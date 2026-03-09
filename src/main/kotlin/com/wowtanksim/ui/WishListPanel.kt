package com.wowtanksim.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.service.WishListData
import com.wowtanksim.service.WishListEntry
import com.wowtanksim.service.WishListService

private data class WishSlotGroup(val name: String, val slots: List<EquipSlot>)

private val wishSlotGroups = listOf(
    WishSlotGroup("Armor", listOf(
        EquipSlot.HEAD, EquipSlot.SHOULDER, EquipSlot.CHEST,
        EquipSlot.WRIST, EquipSlot.HANDS, EquipSlot.WAIST,
        EquipSlot.LEGS, EquipSlot.FEET,
    )),
    WishSlotGroup("Accessories", listOf(
        EquipSlot.NECK, EquipSlot.BACK,
        EquipSlot.RING1, EquipSlot.RING2,
        EquipSlot.TRINKET1, EquipSlot.TRINKET2,
    )),
    WishSlotGroup("Weapons", listOf(
        EquipSlot.MAINHAND, EquipSlot.IDOL,
    )),
)

@Composable
fun WishListPanel(modifier: Modifier = Modifier) {
    var wishListData by remember { mutableStateOf(WishListService.load()) }
    var selectedList by remember { mutableStateOf(wishListData.lists.keys.first()) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<EquipSlot?>(null) }
    var showSlotPicker by remember { mutableStateOf(false) }
    var showNewListDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<String?>(null) }

    val entries = wishListData.lists[selectedList] ?: emptyList()
    val listNames = wishListData.lists.keys.toList()

    fun updateEntries(newEntries: List<WishListEntry>) {
        val newData = WishListData(wishListData.lists + (selectedList to newEntries))
        wishListData = newData
        WishListService.save(newData)
    }

    Card(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Sub-tabs row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ScrollableTabRow(
                    selectedTabIndex = listNames.indexOf(selectedList).coerceAtLeast(0),
                    modifier = Modifier.weight(1f),
                    edgePadding = 0.dp,
                    divider = {},
                ) {
                    listNames.forEachIndexed { _, name ->
                        Tab(
                            selected = name == selectedList,
                            onClick = { selectedList = name },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(name)
                                    // Menu for rename/delete
                                    var showMenu by remember { mutableStateOf(false) }
                                    Box {
                                        TextButton(
                                            onClick = { showMenu = true },
                                            contentPadding = PaddingValues(0.dp),
                                            modifier = Modifier.size(28.dp),
                                        ) {
                                            Text(
                                                "\u25BE",
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = showMenu,
                                            onDismissRequest = { showMenu = false },
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Rename") },
                                                onClick = {
                                                    showMenu = false
                                                    showRenameDialog = name
                                                },
                                            )
                                            if (listNames.size > 1) {
                                                DropdownMenuItem(
                                                    text = { Text("Delete", color = AppColors.removeButton) },
                                                    onClick = {
                                                        showMenu = false
                                                        showDeleteConfirm = name
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                        )
                    }
                }
                TextButton(onClick = { showNewListDialog = true }) {
                    Text("+", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(4.dp))

            // Add item button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = { showSlotPicker = true }) {
                    Text("Add Item")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No items on this wish list yet.\nClick \"Add Item\" to search for items to track.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.inactive,
                    )
                }
            } else {
                val entriesBySlot = entries.groupBy { it.slot }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    wishSlotGroups.forEach { group ->
                        val groupEntries = group.slots.flatMap { slot -> entriesBySlot[slot] ?: emptyList() }
                        if (groupEntries.isNotEmpty()) {
                            item {
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        group.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                            items(groupEntries, key = { "${it.slot}_${it.itemId}" }) { entry ->
                                WishListRow(entry) {
                                    updateEntries(entries.filter { it !== entry })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // New list dialog
    if (showNewListDialog) {
        var newName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showNewListDialog = false },
            title = { Text("New Wish List") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("List name") },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = newName.trim()
                        if (name.isNotEmpty() && name !in wishListData.lists) {
                            val newData = WishListData(wishListData.lists + (name to emptyList()))
                            wishListData = newData
                            WishListService.save(newData)
                            selectedList = name
                        }
                        showNewListDialog = false
                    },
                ) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showNewListDialog = false }) { Text("Cancel") }
            },
        )
    }

    // Rename dialog
    showRenameDialog?.let { oldName ->
        var newName by remember { mutableStateOf(oldName) }
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Rename List") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New name") },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = newName.trim()
                        if (name.isNotEmpty() && (name == oldName || name !in wishListData.lists)) {
                            // Rebuild map preserving order, replacing the key
                            val newLists = wishListData.lists.entries.associate { (k, v) ->
                                if (k == oldName) name to v else k to v
                            }
                            val newData = WishListData(newLists)
                            wishListData = newData
                            WishListService.save(newData)
                            if (selectedList == oldName) selectedList = name
                        }
                        showRenameDialog = null
                    },
                ) { Text("Rename") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) { Text("Cancel") }
            },
        )
    }

    // Delete confirmation
    showDeleteConfirm?.let { nameToDelete ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Delete List") },
            text = { Text("Delete \"$nameToDelete\" and all its items?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newData = WishListData(wishListData.lists - nameToDelete)
                        wishListData = newData
                        WishListService.save(newData)
                        if (selectedList == nameToDelete) {
                            selectedList = newData.lists.keys.first()
                        }
                        showDeleteConfirm = null
                    },
                ) { Text("Delete", color = AppColors.removeButton) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) { Text("Cancel") }
            },
        )
    }

    // Slot picker dialog
    if (showSlotPicker) {
        AlertDialog(
            onDismissRequest = { showSlotPicker = false },
            title = { Text("Select Slot") },
            text = {
                LazyColumn {
                    wishSlotGroups.forEach { group ->
                        item {
                            Text(
                                group.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            )
                        }
                        items(group.slots) { slot ->
                            TextButton(
                                onClick = {
                                    selectedSlot = slot
                                    showSlotPicker = false
                                    showSearchDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(slot.displayName, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSlotPicker = false }) { Text("Cancel") }
            },
        )
    }

    // Reuse item search dialog
    if (showSearchDialog && selectedSlot != null) {
        ItemSearchDialog(
            slot = selectedSlot!!,
            currentItem = null,
            onDismiss = { showSearchDialog = false },
            showEnchantsAndGems = false,
            onItemSelected = { slot, item ->
                val entry = WishListEntry(
                    itemId = item.id,
                    itemName = item.name,
                    slot = slot,
                    iconUrl = item.iconUrl,
                    quality = item.quality,
                    source = "",
                )
                // Avoid duplicates
                if (entries.none { it.itemId == entry.itemId && it.slot == entry.slot }) {
                    updateEntries(entries + entry)
                }
                showSearchDialog = false
            },
        )
    }
}

@Composable
private fun WishListRow(entry: WishListEntry, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            entry.slot.displayName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(80.dp),
            color = AppColors.slotLabel,
        )
        if (entry.iconUrl.isNotBlank()) {
            IconImage(url = entry.iconUrl, modifier = Modifier.padding(end = 6.dp))
        }
        Text(
            entry.itemName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = entry.quality.color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (entry.source.isNotBlank()) {
            Text(
                entry.source,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.inactive,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
        TextButton(
            onClick = onRemove,
            contentPadding = PaddingValues(4.dp),
        ) {
            Text("X", color = AppColors.removeButton, style = MaterialTheme.typography.bodySmall)
        }
    }
}
