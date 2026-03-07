package com.wowtanksim.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.Character
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.service.ArmoryService
import kotlinx.coroutines.launch

@Composable
fun App() {
    var character by remember { mutableStateOf(Character()) }
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<EquipSlot?>(null) }
    var importRegion by remember { mutableStateOf("eu") }
    var importRealm by remember { mutableStateOf("spineshatter") }
    var importName by remember { mutableStateOf("tauroo") }
    var importError by remember { mutableStateOf<String?>(null) }
    var isImporting by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Import bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = importRegion,
                        onValueChange = { importRegion = it },
                        label = { Text("Region") },
                        modifier = Modifier.width(80.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = importRealm,
                        onValueChange = { importRealm = it },
                        label = { Text("Realm") },
                        modifier = Modifier.width(160.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = importName,
                        onValueChange = { importName = it },
                        label = { Text("Character Name") },
                        modifier = Modifier.width(160.dp),
                        singleLine = true,
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                isImporting = true
                                importError = null
                                ArmoryService.fetchCharacter(importRegion, importRealm, importName)
                                    .onSuccess { character = it }
                                    .onFailure { importError = "Import failed: ${it.message}" }
                                isImporting = false
                            }
                        },
                        enabled = !isImporting && importRealm.isNotBlank() && importName.isNotBlank(),
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Text(if (isImporting) "Importing..." else "Import from Armory")
                    }
                    importError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Tab row
                val tabTitles = listOf("Equipment", "Talents")
                TabRow(selectedTabIndex = selectedTab) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) },
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                when (selectedTab) {
                    0 -> {
                        // Equipment tab: items + stats
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            // Left: equipment slots
                            ItemSlotPanel(
                                equipment = character.equipment,
                                setBonuses = character.activeSetBonuses,
                                onSlotClick = { slot ->
                                    selectedSlot = slot
                                    showItemDialog = true
                                },
                                onRemoveItem = { slot ->
                                    character = character.withoutItem(slot)
                                },
                                modifier = Modifier.weight(1f),
                            )

                            // Right: stats + crit immunity
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                val stats = character.aggregateStats()
                                CritImmunityPanel(stats = stats, sotfPoints = character.survivalOfTheFittest)
                                CharacterPanel(stats = stats, character = character)
                            }
                        }
                    }
                    1 -> {
                        // Talents tab
                        TalentTreePanel(
                            talentState = character.talents,
                            onTalentStateChange = { newTalents ->
                                character = character.copy(talents = newTalents)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                DebugConsole(modifier = Modifier.fillMaxWidth())
            }
        }

        // Item search dialog
        if (showItemDialog && selectedSlot != null) {
            ItemSearchDialog(
                slot = selectedSlot!!,
                currentItem = character.equipment[selectedSlot!!],
                onDismiss = { showItemDialog = false },
                onItemSelected = { slot, item ->
                    character = character.withItem(slot, item)
                    showItemDialog = false
                },
            )
        }
    }
}
