package com.wowtanksim.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.*
import com.wowtanksim.service.WowheadService
import kotlinx.coroutines.launch

@Composable
fun ItemSearchDialog(
    slot: EquipSlot,
    currentItem: Item?,
    onDismiss: () -> Unit,
    onItemSelected: (EquipSlot, Item) -> Unit,
) {
    var itemIdText by remember { mutableStateOf("") }
    var fetchedItem by remember { mutableStateOf<Item?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Enchant state
    var enchantIdText by remember { mutableStateOf("") }
    var fetchedEnchant by remember { mutableStateOf<Enchant?>(null) }
    var enchantLoading by remember { mutableStateOf(false) }

    // Gem state: one field per socket
    var gemIdTexts by remember { mutableStateOf(listOf<String>()) }
    var fetchedGems by remember { mutableStateOf(listOf<Gem?>()) }
    var gemLoading by remember { mutableStateOf(false) }

    // When item is fetched, reset gem slots to match socket count
    LaunchedEffect(fetchedItem) {
        fetchedItem?.let { item ->
            gemIdTexts = List(item.numSockets) { "" }
            fetchedGems = List(item.numSockets) { null }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Item - ${slot.displayName}") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                if (currentItem != null) {
                    Text("Current: ${currentItem.name}", style = MaterialTheme.typography.bodySmall, color = currentItem.quality.color)
                }

                // Item lookup
                OutlinedTextField(
                    value = itemIdText,
                    onValueChange = { itemIdText = it.filter { c -> c.isDigit() } },
                    label = { Text("Wowhead Item ID") },
                    placeholder = { Text("e.g. 28825") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Button(
                    onClick = {
                        val id = itemIdText.toIntOrNull()
                        if (id != null && id > 0) {
                            scope.launch {
                                isLoading = true
                                error = null
                                WowheadService.fetchItem(id)
                                    .onSuccess { fetchedItem = it; fetchedEnchant = null }
                                    .onFailure { error = "Failed to fetch: ${it.message}" }
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading && itemIdText.isNotBlank(),
                ) {
                    Text(if (isLoading) "Fetching..." else "Lookup Item")
                }

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                fetchedItem?.let { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)),
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.name, fontWeight = FontWeight.Bold, color = item.quality.color)
                            if (item.stamina > 0) Text("Stamina: ${item.stamina}", style = MaterialTheme.typography.bodySmall)
                            if (item.agility > 0) Text("Agility: ${item.agility}", style = MaterialTheme.typography.bodySmall)
                            if (item.strength > 0) Text("Strength: ${item.strength}", style = MaterialTheme.typography.bodySmall)
                            if (item.armor > 0) Text("Armor: ${item.armor}", style = MaterialTheme.typography.bodySmall)
                            if (item.defenseRating > 0) Text("Defense Rating: ${item.defenseRating}", style = MaterialTheme.typography.bodySmall)
                            if (item.dodgeRating > 0) Text("Dodge Rating: ${item.dodgeRating}", style = MaterialTheme.typography.bodySmall)
                            if (item.resilienceRating > 0) Text("Resilience Rating: ${item.resilienceRating}", style = MaterialTheme.typography.bodySmall)
                            if (item.hitRating > 0) Text("Hit Rating: ${item.hitRating}", style = MaterialTheme.typography.bodySmall)
                            if (item.critRating > 0) Text("Crit Rating: ${item.critRating}", style = MaterialTheme.typography.bodySmall)
                            if (item.attackPower > 0) Text("Attack Power: ${item.attackPower}", style = MaterialTheme.typography.bodySmall)

                            // Socket display
                            if (item.numSockets > 0) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                                Text("Sockets", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                for (i in item.socketTypes.indices) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(gemColorToUiColor(item.socketTypes[i]))
                                        )
                                        OutlinedTextField(
                                            value = gemIdTexts.getOrElse(i) { "" },
                                            onValueChange = { newVal ->
                                                gemIdTexts = gemIdTexts.toMutableList().also {
                                                    while (it.size <= i) it.add("")
                                                    it[i] = newVal.filter { c -> c.isDigit() }
                                                }
                                            },
                                            label = { Text("Gem ID (${item.socketTypes[i].name.lowercase()})") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                        )
                                        val gem = fetchedGems.getOrNull(i)
                                        if (gem != null) {
                                            if (gem.iconUrl.isNotBlank()) {
                                                IconImage(url = gem.iconUrl, size = 18.dp)
                                                Spacer(Modifier.width(4.dp))
                                            }
                                            Text(gem.name, style = MaterialTheme.typography.bodySmall, color = Color(0xFF00CC00))
                                        }
                                    }
                                }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            gemLoading = true
                                            val newGems = gemIdTexts.map { idStr ->
                                                val gid = idStr.toIntOrNull()
                                                if (gid != null && gid > 0) {
                                                    WowheadService.fetchGem(gid).getOrNull()
                                                } else null
                                            }
                                            fetchedGems = newGems
                                            gemLoading = false
                                        }
                                    },
                                    enabled = !gemLoading && gemIdTexts.any { it.isNotBlank() },
                                ) {
                                    Text(if (gemLoading) "Looking up..." else "Lookup Gems")
                                }
                            }

                            // Enchant section
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            Text("Enchant", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                OutlinedTextField(
                                    value = enchantIdText,
                                    onValueChange = { enchantIdText = it.filter { c -> c.isDigit() } },
                                    label = { Text("Enchant Spell ID") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                )
                                Button(
                                    onClick = {
                                        val eid = enchantIdText.toIntOrNull()
                                        if (eid != null && eid > 0) {
                                            scope.launch {
                                                enchantLoading = true
                                                WowheadService.fetchEnchant(eid)
                                                    .onSuccess { fetchedEnchant = it }
                                                    .onFailure { error = "Failed to fetch enchant: ${it.message}" }
                                                enchantLoading = false
                                            }
                                        }
                                    },
                                    enabled = !enchantLoading && enchantIdText.isNotBlank(),
                                ) {
                                    Text(if (enchantLoading) "..." else "Lookup")
                                }
                            }
                            fetchedEnchant?.let { ench ->
                                Text(ench.name, style = MaterialTheme.typography.bodySmall, color = Color(0xFF00CC00))
                            }

                            // Show stat diff vs current
                            if (currentItem != null) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                                Text("Stat Difference:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                showDiff("Stamina", item.stamina - currentItem.stamina)
                                showDiff("Agility", item.agility - currentItem.agility)
                                showDiff("Armor", item.armor - currentItem.armor)
                                showDiff("Def Rating", item.defenseRating - currentItem.defenseRating)
                                showDiff("Dodge Rating", item.dodgeRating - currentItem.dodgeRating)
                                showDiff("Resilience", item.resilienceRating - currentItem.resilienceRating)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    fetchedItem?.let { baseItem ->
                        val finalItem = baseItem.copy(
                            enchant = fetchedEnchant,
                            gems = fetchedGems,
                        )
                        onItemSelected(slot, finalItem)
                    }
                },
                enabled = fetchedItem != null,
            ) {
                Text("Equip Item")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

private fun gemColorToUiColor(color: GemColor): Color = when (color) {
    GemColor.RED -> Color(0xFFFF4444)
    GemColor.BLUE -> Color(0xFF4488FF)
    GemColor.YELLOW -> Color(0xFFFFDD00)
    GemColor.META -> Color(0xFFCCCCCC)
}

@Composable
private fun showDiff(label: String, diff: Int) {
    if (diff != 0) {
        val color = if (diff > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
        val prefix = if (diff > 0) "+" else ""
        Text(
            "$label: $prefix$diff",
            color = color,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
