package com.wowtanksim.service

import com.wowtanksim.model.*
import com.wowtanksim.util.DebugLog
import com.wowtanksim.util.StatParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object WowheadService {

    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 15_000
        }
    }

    private val enchantCache = mutableMapOf<Int, Enchant>()
    private val gemCache = mutableMapOf<Int, Gem>()

    suspend fun fetchItem(itemId: Int): Result<Item> {
        val url = "https://www.wowhead.com/tbc/item=$itemId&xml"
        return try {
            val response = client.get(url) {
                header("User-Agent", "Mozilla/5.0 (WoW Tank Sim)")
            }
            val status = response.status.value
            val xml = response.bodyAsText()

            if (status != 200) {
                DebugLog.error("Wowhead returned HTTP $status for item $itemId")
                return Result.failure(RuntimeException("Wowhead HTTP $status for item $itemId"))
            }

            Result.success(parseItemXml(itemId, xml))
        } catch (e: Exception) {
            DebugLog.error("Wowhead fetch failed for item $itemId", e)
            Result.failure(e)
        }
    }

    /**
     * Fetch enchant data from wowhead spell XML.
     * URL: https://www.wowhead.com/tbc/spell={id}&xml
     * The XML contains a <name> and <jsonEquip> with the enchant stats.
     */
    suspend fun fetchEnchant(enchantId: Int): Result<Enchant> {
        enchantCache[enchantId]?.let { return Result.success(it) }

        val url = "https://www.wowhead.com/tbc/spell=$enchantId&xml"
        return try {
            val response = client.get(url) {
                header("User-Agent", "Mozilla/5.0 (WoW Tank Sim)")
            }
            val status = response.status.value
            val xml = response.bodyAsText()

            if (status != 200) {
                DebugLog.error("Wowhead returned HTTP $status for enchant $enchantId")
                return Result.failure(RuntimeException("Wowhead HTTP $status for enchant $enchantId"))
            }

            val enchant = parseEnchantXml(enchantId, xml)
            enchantCache[enchantId] = enchant
            Result.success(enchant)
        } catch (e: Exception) {
            DebugLog.error("Wowhead fetch failed for enchant $enchantId", e)
            Result.failure(e)
        }
    }

    /**
     * Fetch gem item data from wowhead and return as Gem.
     */
    suspend fun fetchGem(gemId: Int): Result<Gem> {
        gemCache[gemId]?.let { return Result.success(it) }

        val url = "https://www.wowhead.com/tbc/item=$gemId&xml"
        return try {
            val response = client.get(url) {
                header("User-Agent", "Mozilla/5.0 (WoW Tank Sim)")
            }
            val status = response.status.value
            val xml = response.bodyAsText()

            if (status != 200) {
                DebugLog.error("Wowhead returned HTTP $status for gem $gemId")
                return Result.failure(RuntimeException("Wowhead HTTP $status for gem $gemId"))
            }

            val gem = parseGemXml(gemId, xml)
            gemCache[gemId] = gem
            Result.success(gem)
        } catch (e: Exception) {
            DebugLog.error("Wowhead fetch failed for gem $gemId", e)
            Result.failure(e)
        }
    }

    private fun parseItemXml(itemId: Int, xml: String): Item {
        val name = extractTag(xml, "name") ?: "Unknown Item"
        val icon = extractTag(xml, "icon")?.lowercase() ?: ""
        val jsonEquip = extractTag(xml, "jsonEquip") ?: ""
        val qualityId = Regex("""<quality id="(\d+)">""").find(xml)
            ?.groupValues?.get(1)?.toIntOrNull() ?: 1
        val quality = ItemQuality.fromId(qualityId)

        return StatParser.parseJsonEquip(itemId, name, icon, jsonEquip, quality)
    }

    private fun parseEnchantXml(enchantId: Int, xml: String): Enchant {
        val name = extractTag(xml, "name") ?: "Unknown Enchant"
        val jsonEquip = extractTag(xml, "jsonEquip") ?: ""

        val stats = mutableMapOf<String, Int>()
        val regex = Regex(""""?(\w+)"?\s*:\s*(-?\d+)""")
        for (match in regex.findAll(jsonEquip)) {
            stats[match.groupValues[1]] = match.groupValues[2].toIntOrNull() ?: 0
        }

        return Enchant(
            id = enchantId,
            name = name,
            stamina = stats["sta"] ?: 0,
            agility = stats["agi"] ?: 0,
            strength = stats["str"] ?: 0,
            intellect = stats["int"] ?: 0,
            spirit = stats["spi"] ?: 0,
            armor = stats["armor"] ?: 0,
            defenseRating = stats["defrtng"] ?: 0,
            dodgeRating = stats["dodgertng"] ?: 0,
            resilienceRating = stats["resirtng"] ?: 0,
            hitRating = stats["hitrtng"] ?: 0,
            expertiseRating = stats["exprtng"] ?: 0,
            attackPower = stats["atkpwr"] ?: 0,
            critRating = stats["critstrkrtng"] ?: 0,
            hasteRating = stats["hastertng"] ?: 0,
        )
    }

    private fun parseGemXml(gemId: Int, xml: String): Gem {
        val name = extractTag(xml, "name") ?: "Unknown Gem"
        val icon = extractTag(xml, "icon")?.lowercase() ?: ""
        val jsonEquip = extractTag(xml, "jsonEquip") ?: ""

        val stats = mutableMapOf<String, Int>()
        val regex = Regex(""""?(\w+)"?\s*:\s*(-?\d+)""")
        for (match in regex.findAll(jsonEquip)) {
            stats[match.groupValues[1]] = match.groupValues[2].toIntOrNull() ?: 0
        }

        // Determine gem color from subclass or name heuristics
        val color = when {
            name.contains("Meta", ignoreCase = true) -> GemColor.META
            stats.containsKey("nsockets") -> GemColor.META // meta gems have special flags
            name.contains("Delicate", ignoreCase = true) ||
                name.contains("Bold", ignoreCase = true) ||
                name.contains("Bright", ignoreCase = true) -> GemColor.RED
            name.contains("Solid", ignoreCase = true) ||
                name.contains("Sparkling", ignoreCase = true) ||
                name.contains("Lustrous", ignoreCase = true) -> GemColor.BLUE
            name.contains("Smooth", ignoreCase = true) ||
                name.contains("Subtle", ignoreCase = true) ||
                name.contains("Brilliant", ignoreCase = true) -> GemColor.YELLOW
            else -> GemColor.RED // default
        }

        return Gem(
            id = gemId,
            name = name,
            icon = icon,
            color = color,
            stamina = stats["sta"] ?: 0,
            agility = stats["agi"] ?: 0,
            strength = stats["str"] ?: 0,
            intellect = stats["int"] ?: 0,
            spirit = stats["spi"] ?: 0,
            armor = stats["armor"] ?: 0,
            defenseRating = stats["defrtng"] ?: 0,
            dodgeRating = stats["dodgertng"] ?: 0,
            resilienceRating = stats["resirtng"] ?: 0,
            hitRating = stats["hitrtng"] ?: 0,
            expertiseRating = stats["exprtng"] ?: 0,
            attackPower = stats["atkpwr"] ?: 0,
            critRating = stats["critstrkrtng"] ?: 0,
            hasteRating = stats["hastertng"] ?: 0,
        )
    }

    private fun extractTag(xml: String, tag: String): String? {
        val cdataRegex = Regex("<$tag><!\\[CDATA\\[(.+?)]]></$tag>", RegexOption.DOT_MATCHES_ALL)
        cdataRegex.find(xml)?.let { return it.groupValues[1].trim() }

        val simpleRegex = Regex("<$tag[^>]*>(.+?)</$tag>", RegexOption.DOT_MATCHES_ALL)
        simpleRegex.find(xml)?.let { return it.groupValues[1].trim() }

        return null
    }
}
