package com.wowtanksim.model

enum class GemColor { RED, BLUE, YELLOW, META }

data class Gem(
    val id: Int,
    val name: String,
    val icon: String = "",
    val color: GemColor = GemColor.RED,
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
) {
    val iconUrl: String get() = if (icon.isNotBlank()) "https://wow.zamimg.com/images/wow/icons/large/$icon.jpg" else ""
}
