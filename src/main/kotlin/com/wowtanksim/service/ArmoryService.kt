package com.wowtanksim.service

import com.wowtanksim.model.Character
import com.wowtanksim.model.Enchant
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.Item
import com.wowtanksim.util.DebugLog
import com.wowtanksim.util.StatParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

object ArmoryService {

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 30_000
        }
        followRedirects = true
    }

    private val json = Json { ignoreUnknownKeys = true }

    // Blizzard API slot type -> EquipSlot
    private val slotMap = mapOf(
        "HEAD" to EquipSlot.HEAD,
        "NECK" to EquipSlot.NECK,
        "SHOULDER" to EquipSlot.SHOULDER,
        "BACK" to EquipSlot.BACK,
        "CHEST" to EquipSlot.CHEST,
        "WRIST" to EquipSlot.WRIST,
        "HANDS" to EquipSlot.HANDS,
        "WAIST" to EquipSlot.WAIST,
        "LEGS" to EquipSlot.LEGS,
        "FEET" to EquipSlot.FEET,
        "FINGER_1" to EquipSlot.RING1,
        "FINGER_2" to EquipSlot.RING2,
        "TRINKET_1" to EquipSlot.TRINKET1,
        "TRINKET_2" to EquipSlot.TRINKET2,
        "MAIN_HAND" to EquipSlot.MAINHAND,
        "OFF_HAND" to EquipSlot.IDOL,
        "HELD_IN_OFF_HAND" to EquipSlot.IDOL,
        "RANGED" to EquipSlot.IDOL,
    )

    // OAuth2 token cache
    private var accessToken: String? = null
    private var tokenExpiry: Long = 0

    suspend fun fetchCharacter(region: String, realm: String, name: String): Result<Character> {
        return try {
            val config = BattleNetConfig.load().getOrElse { return Result.failure(it) }
            val token = getAccessToken(config).getOrElse { return Result.failure(it) }

            val realmSlug = realm.lowercase().replace(" ", "-").replace("'", "")
            val nameSlug = name.lowercase()
            val namespace = "profile-classicann-$region"
            val url = "https://$region.api.blizzard.com/profile/wow/character/$realmSlug/$nameSlug/equipment" +
                "?namespace=$namespace&locale=en_US"

            DebugLog.info("Fetching Battle.net API: $url")

            val response = client.get(url) {
                header("Authorization", "Bearer $token")
            }
            val status = response.status.value
            val body = response.bodyAsText()

            if (status == 404) {
                return Result.failure(RuntimeException("Character '$name' on '$realm' ($region) not found"))
            }
            if (status == 401 || status == 403) {
                // Invalidate token and retry once
                accessToken = null
                val retryToken = getAccessToken(config).getOrElse { return Result.failure(it) }
                val retryResponse = client.get(url) {
                    header("Authorization", "Bearer $retryToken")
                }
                val retryStatus = retryResponse.status.value
                if (retryStatus != 200) {
                    return Result.failure(
                        RuntimeException("Battle.net API returned HTTP $retryStatus (auth failed). Check your credentials in ~/.wowtanksim/config.json")
                    )
                }
                val retryBody = retryResponse.bodyAsText()
                return parseEquipmentResponse(name, realm, region, retryBody)
            }
            if (status != 200) {
                DebugLog.error("Battle.net API returned HTTP $status")
                DebugLog.error("Response body (first 500 chars): ${body.take(500)}")
                return Result.failure(RuntimeException("Battle.net API returned HTTP $status"))
            }

            parseEquipmentResponse(name, realm, region, body)
        } catch (e: Exception) {
            DebugLog.error("Battle.net API fetch failed", e)
            Result.failure(e)
        }
    }

    private suspend fun getAccessToken(config: BattleNetConfig): Result<String> {
        val now = System.currentTimeMillis()
        accessToken?.let { token ->
            if (now < tokenExpiry) return Result.success(token)
        }

        val tokenUrl = "https://oauth.battle.net/token"
        DebugLog.info("Requesting OAuth2 token from $tokenUrl")

        return try {
            val response = client.submitForm(
                url = tokenUrl,
                formParameters = parameters {
                    append("grant_type", "client_credentials")
                },
            ) {
                basicAuth(config.clientId, config.clientSecret)
            }

            val status = response.status.value
            if (status != 200) {
                DebugLog.error("OAuth2 token request failed with HTTP $status")
                return Result.failure(
                    RuntimeException(
                        "Failed to authenticate with Battle.net (HTTP $status).\n" +
                            "Check your credentials in ~/.wowtanksim/config.json"
                    )
                )
            }

            val body = json.parseToJsonElement(response.bodyAsText()).jsonObject
            val token = body["access_token"]!!.jsonPrimitive.content
            val expiresIn = body["expires_in"]!!.jsonPrimitive.long

            accessToken = token
            tokenExpiry = now + (expiresIn * 1000) - 60_000 // refresh 1 min early
            DebugLog.info("OAuth2 token acquired, expires in ${expiresIn}s")
            Result.success(token)
        } catch (e: Exception) {
            DebugLog.error("OAuth2 token request failed", e)
            Result.failure(RuntimeException("Failed to authenticate with Battle.net: ${e.message}"))
        }
    }

    private suspend fun parseEquipmentResponse(
        name: String,
        realm: String,
        region: String,
        body: String,
    ): Result<Character> {
        val root = json.parseToJsonElement(body).jsonObject
        val equippedItems = root["equipped_items"]?.jsonArray
        if (equippedItems == null) {
            DebugLog.info("No equipped_items in response")
            return Result.success(Character(name = name, realm = realm, region = region))
        }

        DebugLog.info("Found ${equippedItems.size} equipped items")
        val equipment = mutableMapOf<EquipSlot, Item>()

        for (entry in equippedItems) {
            val obj = entry.jsonObject
            val slotType = obj["slot"]?.jsonObject?.get("type")?.jsonPrimitive?.content ?: continue
            val equipSlot = slotMap[slotType]
            if (equipSlot == null) {
                DebugLog.info("Skipping unmapped slot type: $slotType")
                continue
            }

            val itemId = obj["item"]?.jsonObject?.get("id")?.jsonPrimitive?.int ?: continue
            DebugLog.info("  Slot $equipSlot: item=$itemId")

            val baseItem = WowheadService.fetchItem(itemId).getOrNull()
            if (baseItem == null) {
                DebugLog.error("Failed to fetch item $itemId for slot $equipSlot")
                continue
            }

            // Enchantments array contains: permanent enchant (slot 0), gems (slots 2-4), socket bonus (slot 5)
            var enchant: Enchant? = null
            val gems = mutableListOf<com.wowtanksim.model.Gem>()
            val enchantments = obj["enchantments"]?.jsonArray
            if (enchantments != null) {
                for (ench in enchantments) {
                    val enchObj = ench.jsonObject
                    val slotId = enchObj["enchantment_slot"]?.jsonObject?.get("id")?.jsonPrimitive?.int ?: continue
                    val displayString = enchObj["display_string"]?.jsonPrimitive?.content ?: ""
                    val enchantId = enchObj["enchantment_id"]?.jsonPrimitive?.int ?: 0

                    when (slotId) {
                        0 -> {
                            // Permanent enchant — parse stats from display_string
                            DebugLog.info("    Enchant: $displayString")
                            val parsedStats = StatParser.parseStatString(displayString)
                            if (parsedStats.isNotEmpty()) {
                                val sb = StatParser.statKeysToStats(parsedStats)
                                enchant = Enchant(
                                    id = enchantId,
                                    name = displayString.removePrefix("Enchanted: "),
                                    stamina = sb.stamina,
                                    agility = sb.agility,
                                    strength = sb.strength,
                                    intellect = sb.intellect,
                                    spirit = sb.spirit,
                                    armor = sb.armor,
                                    defenseRating = sb.defenseRating,
                                    dodgeRating = sb.dodgeRating,
                                    resilienceRating = sb.resilienceRating,
                                    hitRating = sb.hitRating,
                                    expertiseRating = sb.expertiseRating,
                                    attackPower = sb.attackPower,
                                    critRating = sb.critRating,
                                    hasteRating = sb.hasteRating,
                                )
                            }
                        }
                        in 2..4 -> {
                            // Gem socket — fetch gem by source item ID
                            val gemItemId = enchObj["source_item"]?.jsonObject?.get("id")?.jsonPrimitive?.int
                            if (gemItemId != null && gemItemId > 0) {
                                DebugLog.info("    Gem slot $slotId: item=$gemItemId ($displayString)")
                                val gem = WowheadService.fetchGem(gemItemId).getOrNull()
                                if (gem != null) {
                                    gems.add(gem)
                                }
                            }
                        }
                        5 -> {
                            // Socket bonus — parse stats and merge into enchant
                            val parsedStats = StatParser.parseStatString(displayString)
                            if (parsedStats.isNotEmpty()) {
                                DebugLog.info("    Socket bonus: $displayString")
                                val sb = StatParser.statKeysToStats(parsedStats)
                                val existing = enchant
                                enchant = Enchant(
                                    id = existing?.id ?: 0,
                                    name = if (existing != null) "${existing.name} + $displayString" else displayString,
                                    stamina = (existing?.stamina ?: 0) + sb.stamina,
                                    agility = (existing?.agility ?: 0) + sb.agility,
                                    strength = (existing?.strength ?: 0) + sb.strength,
                                    intellect = (existing?.intellect ?: 0) + sb.intellect,
                                    spirit = (existing?.spirit ?: 0) + sb.spirit,
                                    armor = (existing?.armor ?: 0) + sb.armor,
                                    defenseRating = (existing?.defenseRating ?: 0) + sb.defenseRating,
                                    dodgeRating = (existing?.dodgeRating ?: 0) + sb.dodgeRating,
                                    resilienceRating = (existing?.resilienceRating ?: 0) + sb.resilienceRating,
                                    hitRating = (existing?.hitRating ?: 0) + sb.hitRating,
                                    expertiseRating = (existing?.expertiseRating ?: 0) + sb.expertiseRating,
                                    attackPower = (existing?.attackPower ?: 0) + sb.attackPower,
                                    critRating = (existing?.critRating ?: 0) + sb.critRating,
                                    hasteRating = (existing?.hasteRating ?: 0) + sb.hasteRating,
                                )
                            }
                        }
                    }
                }
            }

            equipment[equipSlot] = baseItem.copy(
                slot = equipSlot,
                enchant = enchant,
                gems = gems,
            )
        }

        // Parse active set bonuses — deduplicate by set ID + required_count
        val seenSetBonuses = mutableSetOf<String>()
        val activeSetBonuses = mutableListOf<SetBonusStat>()
        for (entry in equippedItems) {
            val setData = entry.jsonObject["set"]?.jsonObject ?: continue
            val setName = setData["item_set"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: "Unknown Set"
            val effects = setData["effects"]?.jsonArray ?: continue
            for (effect in effects) {
                val effectObj = effect.jsonObject
                val isActive = effectObj["is_active"]?.jsonPrimitive?.boolean ?: false
                if (!isActive) continue
                val requiredCount = effectObj["required_count"]?.jsonPrimitive?.int ?: 0
                val displayString = effectObj["display_string"]?.jsonPrimitive?.content ?: ""
                val key = "$setName:$requiredCount"
                if (key in seenSetBonuses) continue
                seenSetBonuses.add(key)

                // Parse stat bonuses from display string (e.g. "Set: +35 Resilience Rating.")
                val parsedStats = StatParser.parseStatString(displayString)
                val sb = StatParser.statKeysToStats(parsedStats)
                val description = displayString.removePrefix("Set: ").removeSuffix(".")
                activeSetBonuses.add(SetBonusStat(
                    setName = setName,
                    piecesRequired = requiredCount,
                    description = description,
                    stamina = sb.stamina,
                    agility = sb.agility,
                    strength = sb.strength,
                    armor = sb.armor,
                    defenseRating = sb.defenseRating,
                    dodgeRating = sb.dodgeRating,
                    resilienceRating = sb.resilienceRating,
                    hitRating = sb.hitRating,
                    expertiseRating = sb.expertiseRating,
                    attackPower = sb.attackPower,
                    critRating = sb.critRating,
                ))
                DebugLog.info("  Set bonus: $setName ($requiredCount pc): $description")
            }
        }

        DebugLog.info("Parsed ${equipment.size} items, ${activeSetBonuses.size} set bonuses for $name")
        return Result.success(
            Character(
                name = name,
                realm = realm,
                region = region,
                equipment = equipment,
                activeSetBonuses = activeSetBonuses,
            )
        )
    }
}
