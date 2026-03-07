package com.wowtanksim.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.TankStats
import com.wowtanksim.service.CritCalcService

@Composable
fun CritImmunityPanel(stats: TankStats, sotfPoints: Int, modifier: Modifier = Modifier) {
    val result = CritCalcService.calculate(stats, sotfPoints)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (result.isCritImmune) Color(0xFF1B3A1B) else Color(0xFF3A1B1B)
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                if (result.isCritImmune) "CRIT IMMUNE" else "NOT CRIT IMMUNE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (result.isCritImmune) Color(0xFF4CAF50) else Color(0xFFF44336),
            )

            // Progress bar
            LinearProgressIndicator(
                progress = (result.progressPercent / 100.0).toFloat(),
                modifier = Modifier.fillMaxWidth().height(12.dp),
                color = if (result.isCritImmune) Color(0xFF4CAF50) else Color(0xFFFF9800),
                trackColor = Color(0xFF424242),
            )
            Text(
                "%.1f%% toward crit immunity".format(result.progressPercent),
                style = MaterialTheme.typography.bodySmall,
            )

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Text("Breakdown", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            DetailRow("Boss crit chance (lvl 73)", "%.1f%%".format(result.baseCritChance))
            DetailRow("Survival of the Fittest ($sotfPoints/3)", "-%.1f%%".format(result.sotfReduction))
            DetailRow("Remaining to cover", "%.2f%%".format(result.critToReduce))

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            DetailRow("Defense skill", "%.1f (%.0f rating)".format(result.defenseSkill, stats.defenseRating.toDouble()))
            DetailRow("Crit reduction from defense", "%.2f%%".format(result.critFromDefense))
            DetailRow("Resilience rating", "${stats.resilienceRating}")
            DetailRow("Crit reduction from resilience", "%.2f%%".format(result.critFromResilience))
            DetailRow("Total crit reduction", "%.2f%%".format(result.totalCritReduction))

            Divider(modifier = Modifier.padding(vertical = 4.dp))
            if (result.isCritImmune) {
                val excess = result.totalCritReduction - result.critToReduce
                val excessDefRating = (excess / 0.04) * 2.3654
                val excessResiRating = excess * 39.4
                Text(
                    "Excess: +%.2f%% crit reduction".format(excess),
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Can drop ~%.0f defense rating OR ~%.0f resilience rating".format(excessDefRating, excessResiRating),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA5D6A7),
                )
            } else {
                Text(
                    "Gap: %.2f%% crit reduction still needed".format(result.remainingCritGap),
                    color = Color(0xFFF44336),
                    fontWeight = FontWeight.Bold,
                )
                val defRatingNeeded = (result.remainingCritGap / 0.04) * 2.3654
                val resiRatingNeeded = result.remainingCritGap * 39.4
                Text(
                    "Need ~%.0f more defense rating OR ~%.0f more resilience rating".format(defRatingNeeded, resiRatingNeeded),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFCC80),
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}
