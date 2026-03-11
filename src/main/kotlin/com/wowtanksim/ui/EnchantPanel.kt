package com.wowtanksim.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.*

private val enchantableSlots = listOf(
    EquipSlot.HEAD,
    EquipSlot.SHOULDER,
    EquipSlot.BACK,
    EquipSlot.CHEST,
    EquipSlot.WRIST,
    EquipSlot.HANDS,
    EquipSlot.LEGS,
    EquipSlot.FEET,
    EquipSlot.MAINHAND,
    EquipSlot.RING1,
    EquipSlot.RING2,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnchantPanel(
    equipment: Map<EquipSlot, Item>,
    onApplyEnchant: (EquipSlot, Enchant?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var hideLowTier by remember { mutableStateOf(false) }
    val selectedRoles = remember { mutableStateMapOf<EnchantRole, Boolean>() }
    val expandedSlots = remember { mutableStateMapOf<EquipSlot, Boolean>() }

    // Default: Tank selected
    if (selectedRoles.isEmpty()) {
        selectedRoles[EnchantRole.TANK] = true
    }

    val activeRoles = selectedRoles.filter { it.value }.keys

    Card(modifier = modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Enchants",
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
                // Role filter chips
                EnchantRole.entries.forEach { role ->
                    val selected = selectedRoles[role] == true
                    FilterChip(
                        selected = selected,
                        onClick = { selectedRoles[role] = !selected },
                        label = { Text(role.label) },
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Tier filter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = hideLowTier,
                        onCheckedChange = { hideLowTier = it },
                    )
                    Text(
                        "Hide low-tier",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable { hideLowTier = !hideLowTier },
                    )
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                for (slot in enchantableSlots) {
                    val item = equipment[slot]
                    val expanded = expandedSlots.getOrDefault(slot, true)
                    val options = EnchantData.enchantOptionsForSlot(slot).let { list ->
                        var filtered = list
                        if (hideLowTier) filtered = filtered.filter { it.tier == EnchantTier.HIGH }
                        if (activeRoles.isNotEmpty()) filtered = filtered.filter { option ->
                            option.roles.any { it in activeRoles }
                        }
                        filtered
                    }

                    SlotEnchantSection(
                        slot = slot,
                        item = item,
                        options = options,
                        expanded = expanded,
                        onToggle = { expandedSlots[slot] = !expanded },
                        onSelectEnchant = { enchant -> onApplyEnchant(slot, enchant) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SlotEnchantSection(
    slot: EquipSlot,
    item: Item?,
    options: List<EnchantOption>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelectEnchant: (Enchant?) -> Unit,
) {
    val hasItem = item != null

    // Header row
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
                slot.displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            if (hasItem) {
                Text(
                    " - ${item!!.name}",
                    style = MaterialTheme.typography.titleSmall,
                    color = item.quality.color,
                    maxLines = 1,
                )
            } else {
                Text(
                    " - Empty",
                    style = MaterialTheme.typography.titleSmall,
                    color = AppColors.inactive,
                )
            }
            Spacer(Modifier.weight(1f))
            // Show current enchant name in header when collapsed
            if (!expanded && hasItem && item!!.enchant != null) {
                Text(
                    item.enchant!!.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.enchantGreen,
                    maxLines = 1,
                )
            }
        }
    }

    AnimatedVisibility(visible = expanded) {
        Column(modifier = Modifier.padding(start = 28.dp, top = 2.dp, bottom = 6.dp)) {
            if (!hasItem) {
                Text(
                    "Equip an item first",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = AppColors.inactive,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            } else {
                if (options.isEmpty()) {
                    Text(
                        "No enchants available",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.inactive,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                } else {
                    // "None" option to remove enchant
                    val currentEnchant = item!!.enchant
                    EnchantRow(
                        name = "None",
                        statSummary = "",
                        note = null,
                        isSelected = currentEnchant == null,
                        enabled = true,
                        onClick = { onSelectEnchant(null) },
                    )

                    for (option in options) {
                        val isSelected = currentEnchant?.id == option.enchant.id
                        EnchantRow(
                            name = option.enchant.name,
                            statSummary = option.enchant.statSummary(),
                            note = option.note,
                            source = option.source,
                            materials = option.materials,
                            isSelected = isSelected,
                            enabled = true,
                            onClick = { onSelectEnchant(option.enchant) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnchantRow(
    name: String,
    statSummary: String,
    note: String?,
    source: String = "",
    materials: String = "",
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Applied",
                tint = AppColors.enchantGreen,
                modifier = Modifier.size(16.dp).padding(top = 2.dp),
            )
        } else {
            Spacer(Modifier.size(16.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) AppColors.enchantGreen else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
            if (statSummary.isNotEmpty()) {
                Text(
                    statSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.statSummary,
                )
            }
            if (materials.isNotEmpty()) {
                Text(
                    "Mats: $materials",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.inactive,
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            if (note != null) {
                Text(
                    note,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.inactive,
                )
            }
            if (source.isNotEmpty()) {
                Text(
                    source,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.slotLabel,
                )
            }
        }
    }
}

private fun Enchant.statSummary(): String {
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
