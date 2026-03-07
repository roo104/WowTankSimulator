package com.wowtanksim.service

import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.Item

data class SetBonusStat(
    val setName: String,
    val piecesRequired: Int,
    val description: String,
    val isActive: Boolean = true,
    val stamina: Int = 0,
    val agility: Int = 0,
    val strength: Int = 0,
    val armor: Int = 0,
    val defenseRating: Int = 0,
    val dodgeRating: Int = 0,
    val resilienceRating: Int = 0,
    val hitRating: Int = 0,
    val expertiseRating: Int = 0,
    val attackPower: Int = 0,
    val critRating: Int = 0,
)

object SetBonusService {

    // setId -> list of bonuses at various piece thresholds
    // Set IDs are from wowhead's jsonEquip "itemset" field
    private val setDefinitions: Map<Int, List<SetBonusStat>> = mapOf(
        // Malorne Harness (T4 Feral) - setId 640
        640 to listOf(
            SetBonusStat("Malorne Harness", 2, "Your Mangle grants Clearcasting"),
            // 4pc: +10% bonus attack power in Bear/Cat form (not a simple stat)
        ),
        // Nordrassil Harness (T5 Feral) - setId 665
        665 to listOf(
            SetBonusStat("Nordrassil Harness", 2, "+15 Agility", agility = 15),
        ),
        // Thunderheart Harness (T6 Feral) - setId 676
        676 to listOf(
            SetBonusStat("Thunderheart Harness", 2, "+15 Agility", agility = 15),
        ),
        // Dragonhide Battlegear (PvP honor) - setId 2025
        2025 to listOf(
            SetBonusStat("Dragonhide Battlegear", 2, "+35 Resilience Rating", resilienceRating = 35),
        ),
        // Gladiator's Dragonhide (PvP S1) - setId 586
        586 to listOf(
            SetBonusStat("Gladiator's Dragonhide", 2, "+20 Resilience Rating", resilienceRating = 20),
            SetBonusStat("Gladiator's Dragonhide", 4, "+35 Resilience Rating", resilienceRating = 35),
        ),
        // Merciless Gladiator's Dragonhide (PvP S2) - setId 615
        615 to listOf(
            SetBonusStat("Merciless Gladiator's Dragonhide", 2, "+20 Resilience Rating", resilienceRating = 20),
            SetBonusStat("Merciless Gladiator's Dragonhide", 4, "+35 Resilience Rating", resilienceRating = 35),
        ),
        // Vengeful Gladiator's Dragonhide (PvP S3) - setId 623
        623 to listOf(
            SetBonusStat("Vengeful Gladiator's Dragonhide", 2, "+20 Resilience Rating", resilienceRating = 20),
            SetBonusStat("Vengeful Gladiator's Dragonhide", 4, "+35 Resilience Rating", resilienceRating = 35),
        ),
        // Brutal Gladiator's Dragonhide (PvP S4) - setId 699
        699 to listOf(
            SetBonusStat("Brutal Gladiator's Dragonhide", 2, "+20 Resilience Rating", resilienceRating = 20),
            SetBonusStat("Brutal Gladiator's Dragonhide", 4, "+35 Resilience Rating", resilienceRating = 35),
        ),
        // Heavy Clefthoof set - setId 574
        574 to listOf(
            SetBonusStat("Heavy Clefthoof", 3, "+20 Strength", strength = 20),
        ),
    )

    /**
     * Get all set bonus definitions for a given setId.
     */
    fun getSetBonuses(setId: Int): List<SetBonusStat> = setDefinitions[setId] ?: emptyList()

    /**
     * Count equipped pieces per setId and return all bonus stats with isActive flag.
     */
    fun calculateSetBonuses(equipment: Map<EquipSlot, Item>): List<SetBonusStat> {
        // Count pieces per set
        val setCounts = mutableMapOf<Int, Int>()
        for (item in equipment.values) {
            if (item.setId > 0) {
                setCounts[item.setId] = (setCounts[item.setId] ?: 0) + 1
            }
        }

        val allBonuses = mutableListOf<SetBonusStat>()
        for ((setId, count) in setCounts) {
            val bonuses = setDefinitions[setId] ?: continue
            for (bonus in bonuses) {
                allBonuses.add(bonus.copy(isActive = count >= bonus.piecesRequired))
            }
        }

        return allBonuses
    }

    /**
     * Get all active set bonus descriptions for display.
     */
    fun getActiveSetBonusDescriptions(equipment: Map<EquipSlot, Item>): List<String> {
        val bonuses = calculateSetBonuses(equipment)
        return bonuses.map { "${it.setName} (${it.piecesRequired}pc): ${it.description}" }
    }
}
