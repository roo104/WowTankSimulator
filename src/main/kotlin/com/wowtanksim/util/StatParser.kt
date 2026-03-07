package com.wowtanksim.util

import com.wowtanksim.model.GemColor
import com.wowtanksim.model.Item
import com.wowtanksim.model.ItemQuality

/**
 * Parses the jsonEquip field from wowhead XML item data.
 * Example: "sta":34,"agi":22,"armor":1234,"defrtng":18
 */
object StatParser {

    data class ParsedItemData(
        val item: Item,
        val socketTypes: List<GemColor>,
        val setId: Int,
    )

    fun parseJsonEquip(itemId: Int, name: String, icon: String, jsonEquip: String, quality: ItemQuality = ItemQuality.COMMON): Item {
        val stats = parseKeyValuePairs(jsonEquip)
        val sockets = parseSocketTypes(stats)
        val setId = stats["itemset"] ?: 0

        return Item(
            id = itemId,
            name = name,
            icon = icon,
            quality = quality,
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
            socketTypes = sockets,
            setId = setId,
        )
    }

    private fun parseKeyValuePairs(jsonEquip: String): Map<String, Int> {
        val stats = mutableMapOf<String, Int>()
        val regex = Regex(""""?(\w+)"?\s*:\s*(-?\d+)""")
        for (match in regex.findAll(jsonEquip)) {
            val key = match.groupValues[1]
            val value = match.groupValues[2].toIntOrNull() ?: 0
            stats[key] = value
        }
        return stats
    }

    private fun parseSocketTypes(stats: Map<String, Int>): List<GemColor> {
        val numSockets = stats["nsockets"] ?: 0
        if (numSockets <= 0) return emptyList()
        return (1..numSockets).mapNotNull { i ->
            when (stats["socket$i"]) {
                1 -> GemColor.META
                2 -> GemColor.RED
                4 -> GemColor.YELLOW
                8 -> GemColor.BLUE
                else -> null
            }
        }
    }

    /**
     * Parses armory-style stat description text like:
     * "+34 Attack Power and +16 Hit Rating"
     * "+12 Agility & 3% Increased Critical Damage"
     * "+3 Hit Rating and +3 Agility, +4 Resilience Rating"
     * "+120 Armor"
     * Returns a map of wowhead stat keys to values.
     */
    fun parseStatString(text: String): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        // Match patterns like "+34 Attack Power" or "+12 Stamina"
        // The stat name ends at: comma, " and +", " & ", end of string, or next +digit
        val pattern = Regex("""\+(\d+)\s+([A-Za-z][A-Za-z ]*[A-Za-z])""")
        for (match in pattern.findAll(text)) {
            val value = match.groupValues[1].toIntOrNull() ?: continue
            val statName = match.groupValues[2].trim().lowercase()
            val key = statNameToKey(statName) ?: continue
            result[key] = (result[key] ?: 0) + value
        }
        return result
    }

    private fun statNameToKey(name: String): String? {
        // Trim trailing filler words like "and", "rating"
        val cleaned = name.replace(Regex("""\s+and$"""), "").trim()
        return when {
            cleaned == "stamina" -> "sta"
            cleaned == "agility" -> "agi"
            cleaned == "strength" -> "str"
            cleaned == "intellect" -> "int"
            cleaned == "spirit" -> "spi"
            cleaned.contains("defense") -> "defrtng"
            cleaned.contains("dodge") -> "dodgertng"
            cleaned.contains("resilience") -> "resirtng"
            cleaned.contains("hit") -> "hitrtng"
            cleaned.contains("expertise") -> "exprtng"
            cleaned.contains("attack power") -> "atkpwr"
            cleaned.contains("crit") -> "critstrkrtng"
            cleaned.contains("haste") -> "hastertng"
            cleaned == "armor" -> "armor"
            else -> null
        }
    }

    /** Builds an Enchant-compatible stat map from parsed stat keys */
    fun statKeysToStats(stats: Map<String, Int>): StatBlock = StatBlock(
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

    data class StatBlock(
        val stamina: Int = 0,
        val agility: Int = 0,
        val strength: Int = 0,
        val intellect: Int = 0,
        val spirit: Int = 0,
        val armor: Int = 0,
        val defenseRating: Int = 0,
        val dodgeRating: Int = 0,
        val resilienceRating: Int = 0,
        val hitRating: Int = 0,
        val expertiseRating: Int = 0,
        val attackPower: Int = 0,
        val critRating: Int = 0,
        val hasteRating: Int = 0,
    )
}
