package com.wowtanksim.ui

import androidx.compose.ui.graphics.Color
import com.wowtanksim.model.GemColor

object AppColors {
    // Tooltip surfaces
    val tooltipBackground = Color(0xFF1A1A2E)
    val tooltipDivider = Color(0xFF333355)
    val tooltipLabel = Color(0xFFB0B0B0)
    val tooltipHighlight = Color(0xFFFFD700)

    // Semantic colors
    val positive = Color(0xFF4CAF50)
    val negative = Color(0xFFF44336)
    val warning = Color(0xFFFF9800)
    val enchantGreen = Color(0xFF00CC00)
    val inactive = Color(0xFF808080)

    // Stat summary / secondary text
    val statSummary = Color(0xFF90A4AE)
    val slotLabel = Color(0xFFB0B0B0)
    val emptySlot = Color(0xFF616161)
    val removeButton = Color(0xFFEF5350)

    // Set bonus
    val setBonusTitle = Color(0xFFFFD100)
    val setDivider = Color(0xFF444466)

    // Crit immunity panel
    val critImmuneBackground = Color(0xFF1B3A1B)
    val critVulnerableBackground = Color(0xFF3A1B1B)
    val progressTrack = Color(0xFF424242)
    val excessHint = Color(0xFFA5D6A7)
    val gapHint = Color(0xFFFFCC80)

    // Talent tree
    val talentMaxed = Color(0xFFFFD700)
    val talentAvailable = Color(0xFF00CC00)
    val talentLocked = Color(0xFF666666)
    val talentBgBalance = Color(0xFF0D1526)
    val talentBgFeral = Color(0xFF1A1308)
    val talentBgRestoration = Color(0xFF0A1A0D)
    val talentBgFallback = Color(0xFF111111)
    val talentOverlay = Color(0xCC000000)
    val talentRankText = Color(0xFFAAAAAA)
    val talentDescText = Color(0xFFCCCCCC)

    // Debug console
    val debugBackground = Color(0xFF1A1A1A)
    val debugInfo = Color(0xFF8EC07C)
    val debugError = Color(0xFFFB4934)

    // Icon placeholder
    val iconPlaceholder = Color(0xFF2A2A2A)

    // Gem socket colors
    fun gemSocketColor(color: GemColor): Color = when (color) {
        GemColor.RED -> Color(0xFFFF4444)
        GemColor.BLUE -> Color(0xFF4488FF)
        GemColor.YELLOW -> Color(0xFFFFDD00)
        GemColor.META -> Color(0xFFCCCCCC)
    }
}
