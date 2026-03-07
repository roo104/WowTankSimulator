package com.wowtanksim.model

data class TankStats(
    val stamina: Int = 0,
    val agility: Int = 0,
    val strength: Int = 0,
    val leatherArmor: Int = 0,  // armor from cloth/leather slots (Thick Hide applies)
    val otherArmor: Int = 0,    // armor from cloak, rings, trinkets, weapon, idol
    val defenseRating: Int = 0,
    val dodgeRating: Int = 0,
    val resilienceRating: Int = 0,
    val hitRating: Int = 0,
    val expertiseRating: Int = 0,
    val attackPower: Int = 0,
    val critRating: Int = 0,
    val survivalOfTheFittest: Int = 3,
) {
    // Survival of the Fittest: +1% all attributes per point
    private val sotfMult: Double get() = 1.0 + survivalOfTheFittest * 0.01

    // Total agility in bear form (base + gear + SotF)
    val bearFormAgility: Int get() = ((BASE_AGILITY + agility) * sotfMult).toInt()

    // TBC level 70 conversions
    val defenseSkill: Double get() = 350.0 + kotlin.math.floor(defenseRating / 2.3654)
    val defenseAbove350: Double get() = kotlin.math.floor(defenseRating / 2.3654)
    val critReductionFromDefense: Double get() = defenseAbove350 * 0.04
    val critReductionFromResilience: Double get() = resilienceRating / 39.4
    val dodgeFromRating: Double get() = dodgeRating / 18.9231
    val dodgeFromAgility: Double get() = bearFormAgility / 14.7059 // druid agi-to-dodge at 70
    val baseDodge: Double get() = 5.6 // feral druid base dodge (approximate)
    val totalDodgePercent: Double get() = baseDodge + dodgeFromAgility + dodgeFromRating

    val totalArmor: Int get() = leatherArmor + otherArmor

    // Bear form armor:
    // Dire Bear Form (post-2.3): +400% armor from items (5.0x item armor)
    // Thick Hide 3/3: +10% armor from cloth/leather items only
    // Base armor from agility: 2 armor per agility (not multiplied by bear form or Thick Hide)
    val bearFormArmor: Int get() {
        val agiArmor = bearFormAgility * 2
        val thickHided = leatherArmor * 11 / 10           // Thick Hide 3/3: integer +10%
        val totalItemArmor = thickHided + otherArmor
        val bearItemArmor = totalItemArmor * 5             // Dire Bear Form: integer 5x
        return agiArmor + bearItemArmor
    }
    val armorDamageReduction: Double get() {
        val a = bearFormArmor.toDouble()
        return (a / (a + 10557.5)) * 100.0 // boss level 73 formula
    }

    // Bear form stamina multipliers:
    // Dire Bear Form: +25% stamina (all stamina)
    // Heart of the Wild 5/5: +20% stamina in bear form
    // Survival of the Fittest: +1% all attributes per point
    // Tauren Endurance: +5% stamina
    val bearFormStamina: Int get() {
        val totalRawSta = BASE_STAMINA + stamina
        val direBearMult = 1.25
        val hotwMult = 1.20
        val taurenMult = 1.05
        return (totalRawSta * direBearMult * hotwMult * sotfMult * taurenMult).toInt()
    }

    // Health in bear form
    val bearFormHealth: Int get() {
        val sta = bearFormStamina
        val bonusStamina = (sta - 20).coerceAtLeast(0)
        return BASE_HP + 20 + bonusStamina * 10
    }

    companion object {
        const val BASE_STAMINA = 85  // tauren druid at 70 base stamina
        const val BASE_AGILITY = 65  // tauren druid at 70 base agility
        const val BASE_STRENGTH = 81 // tauren druid at 70 base strength
        const val BASE_HP = 4001     // tauren druid at 70 base HP
    }
}
