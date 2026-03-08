package com.wowtanksim.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.Character
import com.wowtanksim.model.TankStats

@Composable
fun CharacterPanel(
    stats: TankStats,
    character: Character,
    baselineStats: TankStats? = null,
    modifier: Modifier = Modifier,
) {
    val characterName = character.name
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Character Stats - $characterName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Survivability", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            HealthStatRow(stats, baselineStats)
            ArmorStatRow(stats, baselineStats)
            StatRow("Damage Reduction", "%.1f%%".format(stats.armorDamageReduction),
                diff = baselineStats?.let { stats.armorDamageReduction - it.armorDamageReduction })
            DodgeStatRow(stats, character, baselineStats)
            DefenseStatRow(stats, character, baselineStats)
            StatRow("Defense Rating", "${stats.defenseRating}",
                diff = baselineStats?.let { stats.defenseRating - it.defenseRating })
            ResilienceStatRow(stats, character, baselineStats)
            StatRow("Dodge Rating", "${stats.dodgeRating}",
                diff = baselineStats?.let { stats.dodgeRating - it.dodgeRating })

            Spacer(Modifier.height(8.dp))
            Text("Attributes", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            StatRow("Stamina", "${stats.stamina}",
                diff = baselineStats?.let { stats.stamina - it.stamina })
            StatRow("Agility", "${stats.agility}",
                diff = baselineStats?.let { stats.agility - it.agility })
            StatRow("Strength", "${stats.strength}",
                diff = baselineStats?.let { stats.strength - it.strength })
            StatRow("Armor (gear)", "${stats.totalArmor}",
                diff = baselineStats?.let { stats.totalArmor - it.totalArmor })

            Spacer(Modifier.height(8.dp))
            Text("Offense", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            StatRow("Attack Power", "${stats.attackPower}",
                diff = baselineStats?.let { stats.attackPower - it.attackPower })
            StatRow("Hit Rating", "${stats.hitRating}",
                diff = baselineStats?.let { stats.hitRating - it.hitRating })
            StatRow("Crit Rating", "${stats.critRating}",
                diff = baselineStats?.let { stats.critRating - it.critRating })
            StatRow("Expertise Rating", "${stats.expertiseRating}",
                diff = baselineStats?.let { stats.expertiseRating - it.expertiseRating })

            // Set bonuses
            if (character.activeSetBonuses.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("Set Bonuses", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                for (bonus in character.activeSetBonuses) {
                    Text(
                        "${bonus.setName} (${bonus.piecesRequired}pc): ${bonus.description}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (bonus.isActive) AppColors.enchantGreen else AppColors.inactive,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DodgeStatRow(stats: TankStats, character: Character, baselineStats: TankStats? = null) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(6.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).widthIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Dodge Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Base Dodge", "%.2f%%".format(stats.baseDodge))
                    TooltipLine("Agility (${stats.bearFormAgility} / 14.71)", "%.2f%%".format(stats.dodgeFromAgility))
                    TooltipLine("Dodge Rating (${stats.dodgeRating} / 18.92)", "%.2f%%".format(stats.dodgeFromRating))
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Total Dodge", "%.2f%%".format(stats.totalDodgePercent), highlight = true)

                    // Per-item breakdown
                    val itemContribs = character.equipment.filter { (_, item) ->
                        item.effectiveDodgeRating > 0 || item.effectiveAgility > 0
                    }
                    if (itemContribs.isNotEmpty()) {
                        Divider(color = AppColors.tooltipDivider)
                        Text("Per-Item Contributions", style = MaterialTheme.typography.labelSmall, color = AppColors.tooltipLabel)
                        for ((slot, item) in itemContribs) {
                            val parts = mutableListOf<String>()
                            if (item.effectiveAgility > 0) parts += "${item.effectiveAgility} Agi"
                            if (item.effectiveDodgeRating > 0) parts += "${item.effectiveDodgeRating} Dodge"
                            TooltipLine("  ${slot.displayName}", parts.joinToString(", "))
                        }
                    }
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        StatRow("Dodge", "%.2f%%".format(stats.totalDodgePercent),
            diff = baselineStats?.let { stats.totalDodgePercent - it.totalDodgePercent })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DefenseStatRow(stats: TankStats, character: Character, baselineStats: TankStats? = null) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(6.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).widthIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Defense Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Base Defense Skill", "350")
                    TooltipLine("Defense Rating", "${stats.defenseRating}")
                    TooltipLine("Rating Conversion (÷ 2.37)", "+%.1f skill".format(stats.defenseAbove350))
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Total Defense Skill", "%.1f".format(stats.defenseSkill), highlight = true)
                    TooltipLine("Crit Reduction from Defense", "%.2f%%".format(stats.critReductionFromDefense))

                    // Per-item breakdown
                    val itemContribs = character.equipment.filter { (_, item) -> item.effectiveDefenseRating > 0 }
                    if (itemContribs.isNotEmpty()) {
                        Divider(color = AppColors.tooltipDivider)
                        Text("Per-Item Contributions", style = MaterialTheme.typography.labelSmall, color = AppColors.tooltipLabel)
                        for ((slot, item) in itemContribs) {
                            TooltipLine("  ${slot.displayName}", "${item.effectiveDefenseRating} Def")
                        }
                    }
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        StatRow("Defense Skill", "%.1f".format(stats.defenseSkill),
            diff = baselineStats?.let { stats.defenseSkill - it.defenseSkill })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ResilienceStatRow(stats: TankStats, character: Character, baselineStats: TankStats? = null) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(6.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).widthIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Resilience Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Resilience Rating", "${stats.resilienceRating}")
                    TooltipLine("Crit Reduction (÷ 39.4)", "%.2f%%".format(stats.critReductionFromResilience))
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Total Crit Reduction", "%.2f%%".format(stats.critReductionFromResilience), highlight = true)

                    // Per-item breakdown
                    val itemContribs = character.equipment.filter { (_, item) -> item.effectiveResilienceRating > 0 }
                    if (itemContribs.isNotEmpty()) {
                        Divider(color = AppColors.tooltipDivider)
                        Text("Per-Item Contributions", style = MaterialTheme.typography.labelSmall, color = AppColors.tooltipLabel)
                        for ((slot, item) in itemContribs) {
                            TooltipLine("  ${slot.displayName}", "${item.effectiveResilienceRating} Resil")
                        }
                    }
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        StatRow("Resilience Rating", "${stats.resilienceRating}",
            diff = baselineStats?.let { stats.resilienceRating - it.resilienceRating })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HealthStatRow(stats: TankStats, baselineStats: TankStats? = null) {
    val totalRawSta = TankStats.BASE_STAMINA + stats.stamina
    val sotfMult = 1.0 + stats.survivalOfTheFittest * 0.01
    val direBearMult = 1.25
    val hotwMult = 1.0 + stats.heartOfTheWild.coerceIn(0, 5) * 0.04
    val taurenMult = 1.05
    val bearSta = stats.bearFormStamina
    val bonusSta = (bearSta - 20).coerceAtLeast(0)

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Health Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Base Stamina", "${TankStats.BASE_STAMINA}")
                    TooltipLine("Gear Stamina", "${stats.stamina}")
                    TooltipLine("Total Raw Stamina", "$totalRawSta")
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("  Dire Bear Form (×%.2f)".format(direBearMult), "")
                    TooltipLine("  Heart of the Wild ${stats.heartOfTheWild}/5 (×%.2f)".format(hotwMult), "")
                    TooltipLine("  Survival of the Fittest ${stats.survivalOfTheFittest}/3 (×%.2f)".format(sotfMult), "")
                    TooltipLine("  Tauren Endurance (×%.2f)".format(taurenMult), "")
                    TooltipLine("Bear Form Stamina", "$bearSta")
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Base HP", "${TankStats.BASE_HP}")
                    TooltipLine("First 20 Sta (×1 HP)", "20")
                    TooltipLine("Bonus Sta $bonusSta (×10 HP)", "${bonusSta * 10}")
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Bear Form Health", "${stats.bearFormHealth}", highlight = true)
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        StatRow("Health (Bear Form)", "${stats.bearFormHealth}",
            diff = baselineStats?.let { stats.bearFormHealth - it.bearFormHealth })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArmorStatRow(stats: TankStats, baselineStats: TankStats? = null) {
    val bearAgi = stats.bearFormAgility
    val agiArmor = bearAgi * 2
    val thickHideMult = 1.0 + listOf(0.0, 0.04, 0.07, 0.10)[stats.thickHide.coerceIn(0, 3)]
    val thickHided = (stats.leatherArmor * thickHideMult).toInt()
    val totalItemArmor = thickHided + stats.otherArmor
    val bearItemArmor = totalItemArmor * 5

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Armor Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Leather Armor (gear)", "${stats.leatherArmor}")
                    TooltipLine("  Thick Hide ${stats.thickHide}/3 (×%.2f)".format(thickHideMult), "$thickHided")
                    TooltipLine("Other Armor (gear)", "${stats.otherArmor}")
                    TooltipLine("Total Item Armor", "$totalItemArmor")
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("  Dire Bear Form (×5)", "$bearItemArmor")
                    TooltipLine("  Base ${TankStats.BASE_AGILITY} + Gear ${stats.agility} × Survival of the Fittest", "$bearAgi")
                    TooltipLine("  Agility Armor ($bearAgi × 2)", "$agiArmor")
                    Divider(color = AppColors.tooltipDivider)
                    TooltipLine("Bear Form Armor", "${stats.bearFormArmor}", highlight = true)
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        StatRow("Armor (Bear Form)", "${stats.bearFormArmor}",
            diff = baselineStats?.let { stats.bearFormArmor - it.bearFormArmor })
    }
}

@Composable
private fun TooltipLine(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = if (highlight) AppColors.tooltipHighlight else AppColors.tooltipLabel,
        )
        Spacer(Modifier.width(24.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) AppColors.tooltipHighlight else Color.White,
        )
    }
}

@Composable
private fun StatRow(label: String, value: String, diff: Number? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            if (diff != null) {
                val d = diff.toDouble()
                if (d != 0.0) {
                    val isPositive = d > 0
                    val formatted = if (diff is Int || diff is Long) {
                        if (isPositive) "+$diff" else "$diff"
                    } else {
                        if (isPositive) "+%.1f".format(d) else "%.1f".format(d)
                    }
                    Text(
                        formatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isPositive) AppColors.positive else AppColors.negative,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
