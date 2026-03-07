package com.wowtanksim.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.Gem
import com.wowtanksim.model.GemColor
import com.wowtanksim.model.Item
import com.wowtanksim.service.SetBonusService
import com.wowtanksim.service.SetBonusStat

@Composable
fun ItemSlotPanel(
    equipment: Map<EquipSlot, Item>,
    setBonuses: List<SetBonusStat> = emptyList(),
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
                    SlotRow(slot, item, equipment, setBonuses, onSlotClick, onRemoveItem)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SlotRow(
    slot: EquipSlot,
    item: Item?,
    equipment: Map<EquipSlot, Item>,
    setBonuses: List<SetBonusStat>,
    onSlotClick: (EquipSlot) -> Unit,
    onRemoveItem: (EquipSlot) -> Unit,
) {
    val rowContent: @Composable () -> Unit = {
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
                                GemWithTooltip(gem) {
                                    IconImage(url = gem.iconUrl, size = 16.dp)
                                }
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

    if (item != null) {
        TooltipArea(
            tooltip = {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF1A1A2E),
                    shadowElevation = 4.dp,
                ) {
                    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = item.quality.color,
                        )
                        val stats = buildItemStatText(item)
                        if (stats.isNotEmpty()) {
                            Text(
                                stats,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                            )
                        }
                        item.enchant?.let { ench ->
                            Text(
                                ench.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF00CC00),
                            )
                        }
                        if (item.setId > 0) {
                            // Use local definitions first, fall back to API-loaded bonuses
                            val localBonuses = SetBonusService.getSetBonuses(item.setId)
                            val equippedCount = equipment.values.count { it.setId == item.setId }
                            val bonusesToShow = if (localBonuses.isNotEmpty()) {
                                localBonuses.map { it.copy(isActive = equippedCount >= it.piecesRequired) }
                            } else {
                                // Find matching bonuses from API-loaded data by set name
                                val itemSetNames = setBonuses
                                    .filter { equipment.values.any { eq -> eq.setId == item.setId } }
                                    .map { it.setName }
                                    .toSet()
                                setBonuses.filter { it.setName in itemSetNames }
                            }
                            if (bonusesToShow.isNotEmpty()) {
                                Divider(
                                    color = Color(0xFF444466),
                                    modifier = Modifier.padding(vertical = 4.dp),
                                )
                                Text(
                                    "${bonusesToShow.first().setName} ($equippedCount/${bonusesToShow.maxOf { it.piecesRequired }})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD100),
                                )
                                for (bonus in bonusesToShow) {
                                    Text(
                                        "(${bonus.piecesRequired}) Set: ${bonus.description}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (bonus.isActive) Color(0xFF00CC00) else Color(0xFF808080),
                                    )
                                }
                            }
                        }
                    }
                }
            },
            delayMillis = 300,
            tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
        ) {
            rowContent()
        }
    } else {
        rowContent()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GemWithTooltip(gem: Gem, content: @Composable () -> Unit) {
    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF1A1A2E),
                shadowElevation = 4.dp,
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    Text(
                        gem.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    val stats = buildGemStatText(gem)
                    if (stats.isNotEmpty()) {
                        Text(
                            stats,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF00CC00),
                        )
                    }
                }
            }
        },
        delayMillis = 300,
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        content()
    }
}

private fun buildGemStatText(gem: Gem): String {
    val lines = mutableListOf<String>()
    if (gem.stamina != 0) lines += "+${gem.stamina} Stamina"
    if (gem.agility != 0) lines += "+${gem.agility} Agility"
    if (gem.strength != 0) lines += "+${gem.strength} Strength"
    if (gem.intellect != 0) lines += "+${gem.intellect} Intellect"
    if (gem.spirit != 0) lines += "+${gem.spirit} Spirit"
    if (gem.armor != 0) lines += "+${gem.armor} Armor"
    if (gem.defenseRating != 0) lines += "+${gem.defenseRating} Defense Rating"
    if (gem.dodgeRating != 0) lines += "+${gem.dodgeRating} Dodge Rating"
    if (gem.resilienceRating != 0) lines += "+${gem.resilienceRating} Resilience Rating"
    if (gem.hitRating != 0) lines += "+${gem.hitRating} Hit Rating"
    if (gem.expertiseRating != 0) lines += "+${gem.expertiseRating} Expertise Rating"
    if (gem.attackPower != 0) lines += "+${gem.attackPower} Attack Power"
    if (gem.critRating != 0) lines += "+${gem.critRating} Crit Rating"
    if (gem.hasteRating != 0) lines += "+${gem.hasteRating} Haste Rating"
    return lines.joinToString("\n")
}

private fun gemColorToColor(color: GemColor): Color = when (color) {
    GemColor.RED -> Color(0xFFFF4444)
    GemColor.BLUE -> Color(0xFF4488FF)
    GemColor.YELLOW -> Color(0xFFFFDD00)
    GemColor.META -> Color(0xFFCCCCCC)
}

private fun buildItemStatText(item: Item): String {
    val lines = mutableListOf<String>()
    if (item.armor > 0) lines += "${item.armor} Armor"
    if (item.stamina != 0) lines += "+${item.stamina} Stamina"
    if (item.agility != 0) lines += "+${item.agility} Agility"
    if (item.strength != 0) lines += "+${item.strength} Strength"
    if (item.intellect != 0) lines += "+${item.intellect} Intellect"
    if (item.spirit != 0) lines += "+${item.spirit} Spirit"
    if (item.defenseRating != 0) lines += "+${item.defenseRating} Defense Rating"
    if (item.dodgeRating != 0) lines += "+${item.dodgeRating} Dodge Rating"
    if (item.resilienceRating != 0) lines += "+${item.resilienceRating} Resilience Rating"
    if (item.hitRating != 0) lines += "+${item.hitRating} Hit Rating"
    if (item.expertiseRating != 0) lines += "+${item.expertiseRating} Expertise Rating"
    if (item.attackPower != 0) lines += "+${item.attackPower} Attack Power"
    if (item.critRating != 0) lines += "+${item.critRating} Crit Rating"
    if (item.hasteRating != 0) lines += "+${item.hasteRating} Haste Rating"
    return lines.joinToString("\n")
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
