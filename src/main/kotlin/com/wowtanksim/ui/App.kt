package com.wowtanksim.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.Character
import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.TankStats
import com.wowtanksim.service.ArmoryService
import kotlinx.coroutines.launch

private enum class ImportStep(val label: String) {
    IDLE("Import from Armory"),
    AUTHENTICATING("Authenticating..."),
    FETCHING_EQUIPMENT("Fetching equipment..."),
    RESOLVING_ITEMS("Resolving items..."),
    LOADING_TALENTS("Loading talents..."),
}

@Composable
fun App() {
    var character by remember { mutableStateOf(Character()) }
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf<EquipSlot?>(null) }
    var importRegion by remember { mutableStateOf("eu") }
    var importRealm by remember { mutableStateOf("spineshatter") }
    var importName by remember { mutableStateOf("tauroo") }
    var importError by remember { mutableStateOf<String?>(null) }
    var importStep by remember { mutableStateOf(ImportStep.IDLE) }
    var baselineStats by remember { mutableStateOf<TankStats?>(null) }
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
                                importStep = ImportStep.AUTHENTICATING
                                importError = null
                                ArmoryService.fetchCharacter(
                                    region = importRegion,
                                    realm = importRealm,
                                    name = importName,
                                    onProgress = { step ->
                                        importStep = when {
                                            "auth" in step.lowercase() -> ImportStep.AUTHENTICATING
                                            "equipment" in step.lowercase() || "fetching" in step.lowercase() -> ImportStep.FETCHING_EQUIPMENT
                                            "item" in step.lowercase() || "resolv" in step.lowercase() -> ImportStep.RESOLVING_ITEMS
                                            "talent" in step.lowercase() -> ImportStep.LOADING_TALENTS
                                            else -> importStep
                                        }
                                    },
                                )
                                    .onSuccess { character = it; baselineStats = it.aggregateStats() }
                                    .onFailure { importError = "Import failed: ${it.message}" }
                                importStep = ImportStep.IDLE
                            }
                        },
                        enabled = importStep == ImportStep.IDLE && importRealm.isNotBlank() && importName.isNotBlank(),
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        Text(importStep.label)
                    }
                    importError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Tab row
                val tabs = listOf(
                    "Equipment" to Icons.Default.Shield,
                    "Talents" to Icons.Default.Stars,
                    "Wish List" to Icons.Default.Checklist,
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, (title, icon) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) },
                            icon = { Icon(icon, contentDescription = title) },
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

                            // Right: stats + crit immunity OR welcome card
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                if (character.equipment.isEmpty()) {
                                    WelcomeCard(
                                        onImportClick = {
                                            scope.launch {
                                                importStep = ImportStep.AUTHENTICATING
                                                importError = null
                                                ArmoryService.fetchCharacter(
                                                    region = importRegion,
                                                    realm = importRealm,
                                                    name = importName,
                                                    onProgress = { step ->
                                                        importStep = when {
                                                            "auth" in step.lowercase() -> ImportStep.AUTHENTICATING
                                                            "equipment" in step.lowercase() || "fetching" in step.lowercase() -> ImportStep.FETCHING_EQUIPMENT
                                                            "item" in step.lowercase() || "resolv" in step.lowercase() -> ImportStep.RESOLVING_ITEMS
                                                            "talent" in step.lowercase() -> ImportStep.LOADING_TALENTS
                                                            else -> importStep
                                                        }
                                                    },
                                                )
                                                    .onSuccess { character = it; baselineStats = it.aggregateStats() }
                                                    .onFailure { importError = "Import failed: ${it.message}" }
                                                importStep = ImportStep.IDLE
                                            }
                                        },
                                        isImporting = importStep != ImportStep.IDLE,
                                    )
                                } else {
                                    val stats = character.aggregateStats()
                                    CritImmunityPanel(stats = stats, sotfPoints = character.survivalOfTheFittest)
                                    CharacterPanel(stats = stats, character = character, baselineStats = baselineStats)
                                }
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
                    2 -> {
                        // Wish List tab
                        WishListPanel(modifier = Modifier.weight(1f))
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

@Composable
private fun WelcomeCard(onImportClick: () -> Unit, isImporting: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "No Equipment Loaded",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Get started by importing your character from the Armory, or click any equipment slot on the left to manually add items.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onImportClick,
                enabled = !isImporting,
            ) {
                Text(if (isImporting) "Importing..." else "Import from Armory")
            }
            Text(
                "Or click an equipment slot to search for items",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.inactive,
            )
        }
    }
}
