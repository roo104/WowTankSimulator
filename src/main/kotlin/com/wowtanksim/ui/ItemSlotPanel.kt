package com.wowtanksim.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.GemColor
import com.wowtanksim.model.Item

@Composable
fun ItemSlotPanel(
    equipment: Map<EquipSlot, Item>,
    onSlotClick: (EquipSlot) -> Unit,
    onRemoveItem: (EquipSlot) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Equipment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(EquipSlot.entries) { slot ->
                    val item = equipment[slot]
                    SlotRow(slot, item, onSlotClick, onRemoveItem)
                }
            }
        }
    }
}

@Composable
private fun SlotRow(
    slot: EquipSlot,
    item: Item?,
    onSlotClick: (EquipSlot) -> Unit,
    onRemoveItem: (EquipSlot) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSlotClick(slot) }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            slot.displayName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(80.dp),
            color = Color(0xFFB0B0B0),
        )
        if (item != null) {
            if (item.iconUrl.isNotBlank()) {
                IconImage(url = item.iconUrl, modifier = Modifier.padding(end = 6.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = item.quality.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                // Enchant name
                item.enchant?.let { ench ->
                    Text(
                        ench.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00CC00),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                // Gem sockets
                if (item.gems.isNotEmpty() || item.socketTypes.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        val sockets = if (item.socketTypes.isNotEmpty()) item.socketTypes
                            else item.gems.map { it?.color ?: GemColor.RED }
                        for ((i, socketColor) in sockets.withIndex()) {
                            val gem = item.gems.getOrNull(i)
                            if (gem != null && gem.iconUrl.isNotBlank()) {
                                IconImage(url = gem.iconUrl, size = 16.dp)
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(gemColorToColor(socketColor).copy(alpha = 0.3f))
                                )
                            }
                        }
                    }
                }
            }
            // Compact stat summary (effective stats)
            Text(
                buildStatSummary(item),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF90A4AE),
                modifier = Modifier.padding(start = 8.dp),
            )
            TextButton(
                onClick = { onRemoveItem(slot) },
                contentPadding = PaddingValues(4.dp),
            ) {
                Text("X", color = Color(0xFFEF5350), style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Text(
                "Empty - click to add",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun gemColorToColor(color: GemColor): Color = when (color) {
    GemColor.RED -> Color(0xFFFF4444)
    GemColor.BLUE -> Color(0xFF4488FF)
    GemColor.YELLOW -> Color(0xFFFFDD00)
    GemColor.META -> Color(0xFFCCCCCC)
}

private fun buildStatSummary(item: Item): String {
    val parts = mutableListOf<String>()
    if (item.effectiveStamina > 0) parts += "${item.effectiveStamina} Sta"
    if (item.effectiveAgility > 0) parts += "${item.effectiveAgility} Agi"
    if (item.effectiveDefenseRating > 0) parts += "${item.effectiveDefenseRating} Def"
    if (item.effectiveDodgeRating > 0) parts += "${item.effectiveDodgeRating} Dodge"
    if (item.effectiveResilienceRating > 0) parts += "${item.effectiveResilienceRating} Resi"
    if (item.effectiveArmor > 0) parts += "${item.effectiveArmor} Armor"
    return parts.joinToString(" | ")
}
