package com.wowtanksim.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.*

/** Stat types that gems can provide */
private enum class GemStat(val label: String, val matches: (Gem) -> Boolean) {
    STAMINA("Sta", { it.stamina != 0 }),
    AGILITY("Agi", { it.agility != 0 }),
    STRENGTH("Str", { it.strength != 0 }),
    DEFENSE("Def", { it.defenseRating != 0 }),
    DODGE("Dodge", { it.dodgeRating != 0 }),
    HIT("Hit", { it.hitRating != 0 }),
    CRIT("Crit", { it.critRating != 0 }),
    ATTACK_POWER("AP", { it.attackPower != 0 }),
    EXPERTISE("Exp", { it.expertiseRating != 0 }),
    RESILIENCE("Resil", { it.resilienceRating != 0 }),
    INTELLECT("Int", { it.intellect != 0 }),
    SPIRIT("Spi", { it.spirit != 0 }),
}

/** Gem color categories for display sections */
private enum class GemSection(val label: String, val matchesColors: Set<GemColor>) {
    META("Meta", setOf(GemColor.META)),
    RED("Red", setOf(GemColor.RED)),
    YELLOW("Yellow", setOf(GemColor.YELLOW)),
    BLUE("Blue", setOf(GemColor.BLUE)),
    ORANGE("Orange (Red + Yellow)", setOf(GemColor.RED, GemColor.YELLOW)),
    PURPLE("Purple (Red + Blue)", setOf(GemColor.RED, GemColor.BLUE)),
    GREEN("Green (Yellow + Blue)", setOf(GemColor.YELLOW, GemColor.BLUE)),
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GemPanel(
    modifier: Modifier = Modifier,
) {
    var hideUncommon by remember { mutableStateOf(false) }
    var hideCommon by remember { mutableStateOf(true) }
    val selectedColors = remember { mutableStateMapOf<GemColor, Boolean>() }
    val selectedStats = remember { mutableStateMapOf<GemStat, Boolean>() }
    val expandedSections = remember { mutableStateMapOf<GemSection, Boolean>() }

    val activeColors = selectedColors.filter { it.value }.keys
    val activeStats = selectedStats.filter { it.value }.keys

    // Build deduplicated gem lists per section
    val allGemOptions = remember {
        val map = mutableMapOf<GemColor, List<GemOption>>()
        GemColor.entries.forEach { map[it] = GemData.gemOptionsForColor(it) }
        map
    }

    Card(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Gems",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            // Filters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                // Color filter chips
                GemColor.entries.forEach { color ->
                    val selected = selectedColors[color] == true
                    FilterChip(
                        selected = selected,
                        onClick = { selectedColors[color] = !selected },
                        label = { Text(color.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.gemSocketColor(color).copy(alpha = 0.3f),
                        ),
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Hide common filter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hideCommon,
                        onCheckedChange = { hideCommon = it },
                    )
                    Text(
                        "Hide common",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable { hideCommon = !hideCommon },
                    )
                }

                // Hide uncommon filter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hideUncommon,
                        onCheckedChange = { hideUncommon = it },
                    )
                    Text(
                        "Hide uncommon",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable { hideUncommon = !hideUncommon },
                    )
                }
            }

            // Stat filters
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                GemStat.entries.forEach { stat ->
                    val selected = selectedStats[stat] == true
                    FilterChip(
                        selected = selected,
                        onClick = { selectedStats[stat] = !selected },
                        label = { Text(stat.label) },
                        modifier = Modifier.height(30.dp),
                    )
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                for (section in GemSection.entries) {
                    // Determine which gems belong in this section
                    val sectionGems = gemsForSection(section, allGemOptions).let { list ->
                        var filtered = list
                        if (hideCommon) filtered = filtered.filter { it.quality != GemQuality.COMMON }
                        if (hideUncommon) filtered = filtered.filter { it.quality != GemQuality.UNCOMMON }
                        if (activeStats.isNotEmpty()) filtered = filtered.filter { opt ->
                            activeStats.any { stat -> stat.matches(opt.gem) }
                        }
                        filtered.sortedByDescending { it.quality.ordinal }
                    }

                    // Apply color filter: show section if any of its matching colors are selected
                    if (activeColors.isNotEmpty() && section.matchesColors.none { it in activeColors }) {
                        continue
                    }

                    if (sectionGems.isEmpty()) continue

                    val expanded = expandedSections.getOrDefault(section, true)

                    GemSectionHeader(
                        section = section,
                        gemCount = sectionGems.size,
                        expanded = expanded,
                        onToggle = { expandedSections[section] = !expanded },
                    )

                    AnimatedVisibility(visible = expanded) {
                        Column(modifier = Modifier.padding(start = 28.dp, top = 2.dp, bottom = 6.dp)) {
                            for (option in sectionGems) {
                                GemRow(option = option)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Returns gems that belong to a given display section (pure color or hybrid), deduplicated */
private fun gemsForSection(
    section: GemSection,
    allGems: Map<GemColor, List<GemOption>>,
): List<GemOption> {
    return when (section) {
        GemSection.META -> allGems[GemColor.META] ?: emptyList()
        GemSection.RED -> (allGems[GemColor.RED] ?: emptyList()).filter { isPureColor(it, GemColor.RED) }
        GemSection.YELLOW -> (allGems[GemColor.YELLOW] ?: emptyList()).filter { isPureColor(it, GemColor.YELLOW) }
        GemSection.BLUE -> (allGems[GemColor.BLUE] ?: emptyList()).filter { isPureColor(it, GemColor.BLUE) }
        GemSection.ORANGE -> {
            val redIds = (allGems[GemColor.RED] ?: emptyList()).map { it.gem.id }.toSet()
            (allGems[GemColor.YELLOW] ?: emptyList()).filter { it.gem.id in redIds }
        }
        GemSection.PURPLE -> {
            val redIds = (allGems[GemColor.RED] ?: emptyList()).map { it.gem.id }.toSet()
            (allGems[GemColor.BLUE] ?: emptyList()).filter { it.gem.id in redIds }
        }
        GemSection.GREEN -> {
            val yellowIds = (allGems[GemColor.YELLOW] ?: emptyList()).map { it.gem.id }.toSet()
            (allGems[GemColor.BLUE] ?: emptyList()).filter { it.gem.id in yellowIds }
        }
    }
}

/** Check if a gem only appears in one color list (not a hybrid) */
private fun isPureColor(option: GemOption, color: GemColor): Boolean {
    val allColors = GemColor.entries.filter { c ->
        c != color && GemData.gemOptionsForColor(c).any { it.gem.id == option.gem.id }
    }
    return allColors.isEmpty()
}

@Composable
private fun GemSectionHeader(
    section: GemSection,
    gemCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    val sectionColor = when (section) {
        GemSection.META -> AppColors.gemSocketColor(GemColor.META)
        GemSection.RED -> AppColors.gemSocketColor(GemColor.RED)
        GemSection.YELLOW -> AppColors.gemSocketColor(GemColor.YELLOW)
        GemSection.BLUE -> AppColors.gemSocketColor(GemColor.BLUE)
        GemSection.ORANGE -> AppColors.warning
        GemSection.PURPLE -> ItemQuality.EPIC.color
        GemSection.GREEN -> AppColors.positive
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                section.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = sectionColor,
            )
            Spacer(Modifier.weight(1f))
            Text(
                "$gemCount gems",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.inactive,
            )
        }
    }
}

@Composable
private fun GemRow(option: GemOption) {
    val gem = option.gem

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Col 1: Icon + Name
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (gem.iconUrl.isNotBlank()) {
                IconImage(url = gem.iconUrl, size = 20.dp)
            } else {
                Spacer(Modifier.size(20.dp))
            }
            Spacer(Modifier.width(6.dp))
            SelectionContainer {
                Text(
                    gem.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )
            }
        }
        // Col 2: Stats
        Text(
            option.note ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.statSummary,
            modifier = Modifier.weight(0.7f),
            maxLines = 1,
        )
        // Col 3: Source
        Text(
            if (option.source != "Jewelcrafting") option.source else "",
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.slotLabel,
            modifier = Modifier.weight(0.5f),
            maxLines = 1,
        )
    }
}

