package com.wowtanksim.model

data class Enchant(
    val id: Int,
    val name: String,
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
    val spellPower: Int = 0,
    val healingPower: Int = 0,
    val mp5: Int = 0,
    val spellHitRating: Int = 0,
    val spellCritRating: Int = 0,
    val spellPenetration: Int = 0,
    val parryRating: Int = 0,
    val blockRating: Int = 0,
    val blockValue: Int = 0,
) {
    fun statSummary(): String {
        val parts = mutableListOf<String>()
        if (stamina != 0) parts += "+$stamina Sta"
        if (agility != 0) parts += "+$agility Agi"
        if (strength != 0) parts += "+$strength Str"
        if (intellect != 0) parts += "+$intellect Int"
        if (spirit != 0) parts += "+$spirit Spi"
        if (armor != 0) parts += "+$armor Armor"
        if (defenseRating != 0) parts += "+$defenseRating Def"
        if (dodgeRating != 0) parts += "+$dodgeRating Dodge"
        if (resilienceRating != 0) parts += "+$resilienceRating Resil"
        if (hitRating != 0) parts += "+$hitRating Hit"
        if (expertiseRating != 0) parts += "+$expertiseRating Exp"
        if (attackPower != 0) parts += "+$attackPower AP"
        if (critRating != 0) parts += "+$critRating Crit"
        if (hasteRating != 0) parts += "+$hasteRating Haste"
        if (spellPower != 0) parts += "+$spellPower SP"
        if (healingPower != 0) parts += "+$healingPower Heal"
        if (mp5 != 0) parts += "+$mp5 MP5"
        if (spellHitRating != 0) parts += "+$spellHitRating Spell Hit"
        if (spellCritRating != 0) parts += "+$spellCritRating Spell Crit"
        if (spellPenetration != 0) parts += "+$spellPenetration Spell Pen"
        if (parryRating != 0) parts += "+$parryRating Parry"
        if (blockRating != 0) parts += "+$blockRating Block"
        if (blockValue != 0) parts += "+$blockValue Block Value"
        return parts.joinToString(", ")
    }
}
