package com.wowtanksim.model

import com.wowtanksim.service.SetBonusStat

data class Character(
    val name: String = "Unknown",
    val realm: String = "",
    val region: String = "eu",
    val level: Int = 70,
    val equipment: Map<EquipSlot, Item> = emptyMap(),
    val survivalOfTheFittest: Int = 3, // 0-3 talent points
    val activeSetBonuses: List<SetBonusStat> = emptyList(),
) {
    fun aggregateStats(): TankStats {
        var sta = 0; var agi = 0; var str = 0
        var leatherArmor = 0; var otherArmor = 0
        var defRating = 0; var dodgeRating = 0; var resiRating = 0
        var hitRating = 0; var expRating = 0; var ap = 0; var critRating = 0

        for ((slot, item) in equipment) {
            // effective* includes base + gems + enchant
            sta += item.effectiveStamina
            agi += item.effectiveAgility
            str += item.effectiveStrength
            if (slot.isArmor) {
                leatherArmor += item.effectiveArmor
            } else {
                otherArmor += item.effectiveArmor
            }
            defRating += item.effectiveDefenseRating
            dodgeRating += item.effectiveDodgeRating
            resiRating += item.effectiveResilienceRating
            hitRating += item.effectiveHitRating
            expRating += item.effectiveExpertiseRating
            ap += item.effectiveAttackPower
            critRating += item.effectiveCritRating
        }

        // Add set bonus stats
        for (bonus in activeSetBonuses) {
            sta += bonus.stamina
            agi += bonus.agility
            str += bonus.strength
            leatherArmor += bonus.armor
            defRating += bonus.defenseRating
            dodgeRating += bonus.dodgeRating
            resiRating += bonus.resilienceRating
            hitRating += bonus.hitRating
            expRating += bonus.expertiseRating
            ap += bonus.attackPower
            critRating += bonus.critRating
        }

        return TankStats(
            stamina = sta,
            agility = agi,
            strength = str,
            leatherArmor = leatherArmor,
            otherArmor = otherArmor,
            defenseRating = defRating,
            dodgeRating = dodgeRating,
            resilienceRating = resiRating,
            hitRating = hitRating,
            expertiseRating = expRating,
            attackPower = ap,
            critRating = critRating,
            survivalOfTheFittest = survivalOfTheFittest,
        )
    }

    fun withItem(slot: EquipSlot, item: Item): Character {
        return copy(equipment = equipment + (slot to item.copy(slot = slot)))
    }

    fun withoutItem(slot: EquipSlot): Character {
        return copy(equipment = equipment - slot)
    }
}
