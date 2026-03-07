package com.wowtanksim.model

import androidx.compose.ui.graphics.Color

enum class ItemQuality(val color: Color) {
    POOR(Color(0xFF9D9D9D)),        // Grey
    COMMON(Color(0xFFFFFFFF)),      // White
    UNCOMMON(Color(0xFF1EFF00)),    // Green
    RARE(Color(0xFF0070DD)),        // Blue
    EPIC(Color(0xFFA335EE)),        // Purple
    LEGENDARY(Color(0xFFFF8000)),   // Orange
    ARTIFACT(Color(0xFFE6CC80)),    // Light gold
    HEIRLOOM(Color(0xFFE6CC80));    // Light gold

    companion object {
        fun fromId(id: Int): ItemQuality = entries.getOrElse(id) { COMMON }
    }
}

data class Item(
    val id: Int,
    val name: String,
    val icon: String = "",
    val quality: ItemQuality = ItemQuality.COMMON,
    val slot: EquipSlot? = null,
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
    // Sockets
    val socketTypes: List<GemColor> = emptyList(),
    val gems: List<Gem?> = emptyList(),
    // Enchant
    val enchant: Enchant? = null,
    // Set
    val setId: Int = 0,
) {
    val iconUrl: String get() = if (icon.isNotBlank()) "https://wow.zamimg.com/images/wow/icons/large/$icon.jpg" else ""
    val numSockets: Int get() = socketTypes.size

    /** Total stats from all socketed gems */
    val totalGemStamina: Int get() = gems.filterNotNull().sumOf { it.stamina }
    val totalGemAgility: Int get() = gems.filterNotNull().sumOf { it.agility }
    val totalGemStrength: Int get() = gems.filterNotNull().sumOf { it.strength }
    val totalGemArmor: Int get() = gems.filterNotNull().sumOf { it.armor }
    val totalGemDefenseRating: Int get() = gems.filterNotNull().sumOf { it.defenseRating }
    val totalGemDodgeRating: Int get() = gems.filterNotNull().sumOf { it.dodgeRating }
    val totalGemResilienceRating: Int get() = gems.filterNotNull().sumOf { it.resilienceRating }
    val totalGemHitRating: Int get() = gems.filterNotNull().sumOf { it.hitRating }
    val totalGemExpertiseRating: Int get() = gems.filterNotNull().sumOf { it.expertiseRating }
    val totalGemAttackPower: Int get() = gems.filterNotNull().sumOf { it.attackPower }
    val totalGemCritRating: Int get() = gems.filterNotNull().sumOf { it.critRating }

    /** Effective stats = base + gems + enchant */
    val effectiveStamina: Int get() = stamina + totalGemStamina + (enchant?.stamina ?: 0)
    val effectiveAgility: Int get() = agility + totalGemAgility + (enchant?.agility ?: 0)
    val effectiveStrength: Int get() = strength + totalGemStrength + (enchant?.strength ?: 0)
    val effectiveArmor: Int get() = armor + totalGemArmor + (enchant?.armor ?: 0)
    val effectiveDefenseRating: Int get() = defenseRating + totalGemDefenseRating + (enchant?.defenseRating ?: 0)
    val effectiveDodgeRating: Int get() = dodgeRating + totalGemDodgeRating + (enchant?.dodgeRating ?: 0)
    val effectiveResilienceRating: Int get() = resilienceRating + totalGemResilienceRating + (enchant?.resilienceRating ?: 0)
    val effectiveHitRating: Int get() = hitRating + totalGemHitRating + (enchant?.hitRating ?: 0)
    val effectiveExpertiseRating: Int get() = expertiseRating + totalGemExpertiseRating + (enchant?.expertiseRating ?: 0)
    val effectiveAttackPower: Int get() = attackPower + totalGemAttackPower + (enchant?.attackPower ?: 0)
    val effectiveCritRating: Int get() = critRating + totalGemCritRating + (enchant?.critRating ?: 0)
}
