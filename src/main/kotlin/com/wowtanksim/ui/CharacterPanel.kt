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
            HealthStatRow(stats)
            ArmorStatRow(stats)
            StatRow("Damage Reduction", "%.1f%%".format(stats.armorDamageReduction))
            StatRow("Dodge", "%.2f%%".format(stats.totalDodgePercent))
            StatRow("Defense Skill", "%.1f".format(stats.defenseSkill))
            StatRow("Defense Rating", "${stats.defenseRating}")
            StatRow("Resilience Rating", "${stats.resilienceRating}")
            StatRow("Dodge Rating", "${stats.dodgeRating}")

            Spacer(Modifier.height(8.dp))
            Text("Attributes", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            StatRow("Stamina", "${stats.stamina}")
            StatRow("Agility", "${stats.agility}")
            StatRow("Strength", "${stats.strength}")
            StatRow("Armor (gear)", "${stats.totalArmor}")

            Spacer(Modifier.height(8.dp))
            Text("Offense", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            StatRow("Attack Power", "${stats.attackPower}")
            StatRow("Hit Rating", "${stats.hitRating}")
            StatRow("Crit Rating", "${stats.critRating}")
            StatRow("Expertise Rating", "${stats.expertiseRating}")

            // Set bonuses
            if (character.activeSetBonuses.isNotEmpty()) {
                val bonusDescs = character.activeSetBonuses.map { "${it.setName} (${it.piecesRequired}pc): ${it.description}" }
                if (bonusDescs.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Set Bonuses", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    for (desc in bonusDescs) {
                        Text(desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF00CC00))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HealthStatRow(stats: TankStats) {
    val totalRawSta = TankStats.BASE_STAMINA + stats.stamina
    val sotfMult = 1.0 + stats.survivalOfTheFittest * 0.01
    val direBearMult = 1.25
    val hotwMult = 1.20
    val taurenMult = 1.05
    val bearSta = stats.bearFormStamina
    val bonusSta = (bearSta - 20).coerceAtLeast(0)

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF1A1A2E),
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Health Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = Color(0xFF333355))
                    TooltipLine("Base Stamina", "${TankStats.BASE_STAMINA}")
                    TooltipLine("Gear Stamina", "${stats.stamina}")
                    TooltipLine("Total Raw Stamina", "$totalRawSta")
                    Divider(color = Color(0xFF333355))
                    TooltipLine("  Dire Bear Form (×%.2f)".format(direBearMult), "")
                    TooltipLine("  Heart of the Wild (×%.2f)".format(hotwMult), "")
                    TooltipLine("  Survival of the Fittest ${stats.survivalOfTheFittest}/3 (×%.2f)".format(sotfMult), "")
                    TooltipLine("  Tauren Endurance (×%.2f)".format(taurenMult), "")
                    TooltipLine("Bear Form Stamina", "$bearSta")
                    Divider(color = Color(0xFF333355))
                    TooltipLine("Base HP", "${TankStats.BASE_HP}")
                    TooltipLine("First 20 Sta (×1 HP)", "20")
                    TooltipLine("Bonus Sta $bonusSta (×10 HP)", "${bonusSta * 10}")
                    Divider(color = Color(0xFF333355))
                    TooltipLine("Bear Form Health", "${stats.bearFormHealth}", highlight = true)
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Health (Bear Form)", style = MaterialTheme.typography.bodyMedium)
            Text("${stats.bearFormHealth}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArmorStatRow(stats: TankStats) {
    val bearAgi = stats.bearFormAgility
    val agiArmor = bearAgi * 2
    val thickHided = stats.leatherArmor * 11 / 10
    val totalItemArmor = thickHided + stats.otherArmor
    val bearItemArmor = totalItemArmor * 5

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF1A1A2E),
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Armor Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Divider(color = Color(0xFF333355))
                    TooltipLine("Leather Armor (gear)", "${stats.leatherArmor}")
                    TooltipLine("  Thick Hide 3/3 (×1.1)", "$thickHided")
                    TooltipLine("Other Armor (gear)", "${stats.otherArmor}")
                    TooltipLine("Total Item Armor", "$totalItemArmor")
                    Divider(color = Color(0xFF333355))
                    TooltipLine("  Dire Bear Form (×5)", "$bearItemArmor")
                    TooltipLine("  Base ${TankStats.BASE_AGILITY} + Gear ${stats.agility} × Survival of the Fittest", "$bearAgi")
                    TooltipLine("  Agility Armor ($bearAgi × 2)", "$agiArmor")
                    Divider(color = Color(0xFF333355))
                    TooltipLine("Bear Form Armor", "${stats.bearFormArmor}", highlight = true)
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Armor (Bear Form)", style = MaterialTheme.typography.bodyMedium)
            Text("${stats.bearFormArmor}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
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
            color = if (highlight) Color(0xFFFFD700) else Color(0xFFB0B0B0),
        )
        Spacer(Modifier.width(24.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) Color(0xFFFFD700) else Color.White,
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
