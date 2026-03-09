package com.wowtanksim.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.wowtanksim.model.*
import com.wowtanksim.service.WowheadSearchResult
import com.wowtanksim.service.WowheadService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ItemSearchDialog(
    slot: EquipSlot,
    currentItem: Item?,
    onDismiss: () -> Unit,
    onItemSelected: (EquipSlot, Item) -> Unit,
    showEnchantsAndGems: Boolean = true,
) {
    var fetchedItem by remember { mutableStateOf<Item?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var useManualItemEntry by remember { mutableStateOf(false) }
    var itemIdText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Enchant state
    var enchantIdText by remember { mutableStateOf("") }
    var fetchedEnchant by remember { mutableStateOf<Enchant?>(null) }
    var enchantLoading by remember { mutableStateOf(false) }
    var enchantExpanded by remember { mutableStateOf(false) }
    var useManualEnchant by remember { mutableStateOf(false) }
    val enchantOptions = remember(slot) { EnchantData.enchantOptionsForSlot(slot) }
    val slotHasEnchants = enchantOptions.isNotEmpty()

    // Gem state: one entry per socket
    var fetchedGems by remember { mutableStateOf(listOf<Gem?>()) }
    var gemExpanded by remember { mutableStateOf(listOf<Boolean>()) }
    var useManualGem by remember { mutableStateOf(listOf<Boolean>()) }
    var gemIdTexts by remember { mutableStateOf(listOf<String>()) }
    var gemLoading by remember { mutableStateOf(false) }

    // Tier filter state
    var selectedTier by remember { mutableStateOf<ContentTier?>(null) }

    // Wowhead search state
    var wowheadResults by remember { mutableStateOf<List<WowheadSearchResult>>(emptyList()) }
    var wowheadSearching by remember { mutableStateOf(false) }
    var wowheadSearchJob by remember { mutableStateOf<Job?>(null) }

    // Debounced Wowhead search when query changes
    LaunchedEffect(searchQuery) {
        wowheadSearchJob?.cancel()
        if (searchQuery.length >= 3) {
            wowheadSearchJob = scope.launch {
                delay(400) // debounce
                wowheadSearching = true
                WowheadService.searchItems(searchQuery)
                    .onSuccess { wowheadResults = it }
                    .onFailure { wowheadResults = emptyList() }
                wowheadSearching = false
            }
        } else {
            wowheadResults = emptyList()
            wowheadSearching = false
        }
    }

    // Database items for slot
    val databaseItems = remember(slot) { ItemDatabase.itemsForSlot(slot).sortedByDescending { it.item.ilvl } }

    // When item is fetched, reset gem slots
    LaunchedEffect(fetchedItem) {
        fetchedItem?.let { item ->
            fetchedGems = List(item.numSockets) { null }
            gemExpanded = List(item.numSockets) { false }
            useManualGem = List(item.numSockets) { false }
            gemIdTexts = List(item.numSockets) { "" }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Item - ${slot.displayName}") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState()).widthIn(min = 400.dp),
            ) {
                if (currentItem != null) {
                    Text("Current: ${currentItem.name}", style = MaterialTheme.typography.bodySmall, color = currentItem.quality.color)
                }

                if (!useManualItemEntry) {
                    // Database item search
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search items...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    // Tier filter chips
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Tier:", style = MaterialTheme.typography.labelSmall, color = AppColors.inactive)
                        ContentTier.entries.forEach { tier ->
                            FilterChip(
                                selected = selectedTier == tier,
                                onClick = { selectedTier = if (selectedTier == tier) null else tier },
                                label = { Text(tier.displayName, style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.height(28.dp),
                            )
                        }
                    }

                    val tierFiltered = if (selectedTier != null) databaseItems.filter { it.item.ilvl <= selectedTier!!.maxIlvl } else databaseItems
                    val filteredItems = if (searchQuery.isBlank()) tierFiltered
                    else tierFiltered.filter { it.item.name.contains(searchQuery, ignoreCase = true) || it.source.contains(searchQuery, ignoreCase = true) }

                    // Filter out Wowhead results that are already in the local DB
                    val localItemIds = filteredItems.map { it.item.id }.toSet()
                    val filteredWowheadResults = wowheadResults.filter { it.id !in localItemIds }

                    Card(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 250.dp),
                        colors = CardDefaults.cardColors(containerColor = AppColors.tooltipBackground),
                    ) {
                        if (filteredItems.isEmpty() && filteredWowheadResults.isEmpty() && !wowheadSearching) {
                            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    if (searchQuery.length in 1..2) "Type 3+ characters to search Wowhead"
                                    else "No items found",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.inactive,
                                )
                            }
                        } else {
                            LazyColumn {
                                items(filteredItems) { option ->
                                    ItemDatabaseRow(option) {
                                        fetchedItem = option.item.copy(slot = slot)
                                        fetchedEnchant = null
                                        error = null
                                    }
                                }
                                if (filteredWowheadResults.isNotEmpty()) {
                                    item {
                                        Text(
                                            "Wowhead Results",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AppColors.inactive,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        )
                                    }
                                    items(filteredWowheadResults) { result ->
                                        WowheadSearchRow(result) {
                                            scope.launch {
                                                isLoading = true
                                                error = null
                                                WowheadService.fetchItem(result.id)
                                                    .onSuccess { fetchedItem = it.copy(slot = slot); fetchedEnchant = null }
                                                    .onFailure { error = "Failed to fetch: ${it.message}" }
                                                isLoading = false
                                            }
                                        }
                                    }
                                }
                                if (wowheadSearching) {
                                    item {
                                        Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                                            Text("Searching Wowhead...", style = MaterialTheme.typography.bodySmall, color = AppColors.inactive)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    Text(
                        "Enter Wowhead ID Manually",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { useManualItemEntry = true }.padding(top = 4.dp),
                    )
                } else {
                    // Manual Wowhead ID entry
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

                    Text(
                        "Choose from List",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { useManualItemEntry = false }.padding(top = 4.dp),
                    )
                }

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                fetchedItem?.let { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AppColors.tooltipBackground),
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (item.iconUrl.isNotBlank()) {
                                    IconImage(url = item.iconUrl, size = 32.dp)
                                }
                                Column {
                                    Text(item.name, fontWeight = FontWeight.Bold, color = item.quality.color)
                                    if (item.ilvl > 0) {
                                        Text("Item Level ${item.ilvl}", style = MaterialTheme.typography.bodySmall, color = AppColors.tooltipLabel)
                                    }
                                }
                            }
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

                            // Socket display with gem dropdowns
                            if (showEnchantsAndGems && item.numSockets > 0) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                                Text("Sockets", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                for (i in item.socketTypes.indices) {
                                    val socketColor = item.socketTypes[i]
                                    val gemOptions = remember(socketColor) { GemData.gemOptionsForColor(socketColor) }
                                    val isManual = useManualGem.getOrElse(i) { false }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(AppColors.gemSocketColor(socketColor))
                                        )

                                        if (!isManual && gemOptions.isNotEmpty()) {
                                            // Dropdown gem selector
                                            val expanded = gemExpanded.getOrElse(i) { false }
                                            ExposedDropdownMenuBox(
                                                expanded = expanded,
                                                onExpandedChange = { newVal ->
                                                    gemExpanded = gemExpanded.toMutableList().also {
                                                        while (it.size <= i) it.add(false)
                                                        it[i] = newVal
                                                    }
                                                },
                                                modifier = Modifier.weight(1f),
                                            ) {
                                                OutlinedTextField(
                                                    value = fetchedGems.getOrNull(i)?.name ?: "None",
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    singleLine = true,
                                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                                                    textStyle = MaterialTheme.typography.bodySmall,
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = {
                                                        gemExpanded = gemExpanded.toMutableList().also { it[i] = false }
                                                    },
                                                ) {
                                                    DropdownMenuItem(
                                                        text = { Text("None") },
                                                        onClick = {
                                                            fetchedGems = fetchedGems.toMutableList().also { it[i] = null }
                                                            gemExpanded = gemExpanded.toMutableList().also { it[i] = false }
                                                        },
                                                    )
                                                    gemOptions.forEach { option ->
                                                        DropdownMenuItem(
                                                            text = {
                                                                Column {
                                                                    Text(option.gem.name, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodySmall)
                                                                    option.note?.let {
                                                                        Text(it, style = MaterialTheme.typography.bodySmall, color = AppColors.positive)
                                                                    }
                                                                }
                                                            },
                                                            onClick = {
                                                                fetchedGems = fetchedGems.toMutableList().also { it[i] = option.gem }
                                                                gemExpanded = gemExpanded.toMutableList().also { it[i] = false }
                                                            },
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                "ID",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.clickable {
                                                    useManualGem = useManualGem.toMutableList().also {
                                                        while (it.size <= i) it.add(false)
                                                        it[i] = true
                                                    }
                                                },
                                            )
                                        } else {
                                            // Manual gem ID entry
                                            OutlinedTextField(
                                                value = gemIdTexts.getOrElse(i) { "" },
                                                onValueChange = { newVal ->
                                                    gemIdTexts = gemIdTexts.toMutableList().also {
                                                        while (it.size <= i) it.add("")
                                                        it[i] = newVal.filter { c -> c.isDigit() }
                                                    }
                                                },
                                                label = { Text("Gem ID (${socketColor.name.lowercase()})") },
                                                singleLine = true,
                                                modifier = Modifier.weight(1f),
                                                textStyle = MaterialTheme.typography.bodySmall,
                                            )
                                            val gem = fetchedGems.getOrNull(i)
                                            if (gem != null) {
                                                if (gem.iconUrl.isNotBlank()) {
                                                    IconImage(url = gem.iconUrl, size = 18.dp)
                                                }
                                                Text(gem.name, style = MaterialTheme.typography.bodySmall, color = AppColors.enchantGreen)
                                            }
                                            if (gemOptions.isNotEmpty()) {
                                                Text(
                                                    "List",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.clickable {
                                                        useManualGem = useManualGem.toMutableList().also {
                                                            while (it.size <= i) it.add(false)
                                                            it[i] = false
                                                        }
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }
                                // Lookup button for manual gem IDs
                                if (useManualGem.any { it }) {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                gemLoading = true
                                                val newGems = fetchedGems.toMutableList()
                                                for (idx in gemIdTexts.indices) {
                                                    if (useManualGem.getOrElse(idx) { false }) {
                                                        val gid = gemIdTexts.getOrElse(idx) { "" }.toIntOrNull()
                                                        if (gid != null && gid > 0) {
                                                            newGems[idx] = WowheadService.fetchGem(gid).getOrNull()
                                                        }
                                                    }
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
                            }

                            // Enchant section
                            if (showEnchantsAndGems && (slotHasEnchants || useManualEnchant)) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                                Text("Enchant", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)

                                if (!useManualEnchant && slotHasEnchants) {
                                    ExposedDropdownMenuBox(
                                        expanded = enchantExpanded,
                                        onExpandedChange = { enchantExpanded = it },
                                    ) {
                                        OutlinedTextField(
                                            value = fetchedEnchant?.name ?: "None",
                                            onValueChange = {},
                                            readOnly = true,
                                            singleLine = true,
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = enchantExpanded) },
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                        )
                                        ExposedDropdownMenu(
                                            expanded = enchantExpanded,
                                            onDismissRequest = { enchantExpanded = false },
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("None") },
                                                onClick = {
                                                    fetchedEnchant = null
                                                    enchantExpanded = false
                                                },
                                            )
                                            enchantOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Column {
                                                            Text(option.enchant.name, fontWeight = FontWeight.Medium)
                                                            Text(
                                                                buildEnchantStatSummary(option),
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = AppColors.positive,
                                                            )
                                                        }
                                                    },
                                                    onClick = {
                                                        fetchedEnchant = option.enchant
                                                        enchantExpanded = false
                                                    },
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        "Enter ID Manually",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable { useManualEnchant = true }.padding(top = 4.dp),
                                    )
                                } else {
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
                                        Text(ench.name, style = MaterialTheme.typography.bodySmall, color = AppColors.enchantGreen)
                                    }
                                    if (slotHasEnchants) {
                                        Text(
                                            "Choose from List",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable { useManualEnchant = false }.padding(top = 4.dp),
                                        )
                                    }
                                }
                            }

                            // Stat diff vs current
                            if (showEnchantsAndGems && currentItem != null) {
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

@Composable
private fun WowheadSearchRow(result: WowheadSearchResult, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (result.icon.isNotBlank()) {
            IconImage(url = "https://wow.zamimg.com/images/wow/icons/small/${result.icon}.jpg", size = 24.dp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                result.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = result.quality.color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            "#${result.id}",
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.inactive,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemDatabaseRow(option: ItemOption, onClick: () -> Unit) {
    val item = option.item
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(4.dp),
                color = AppColors.tooltipBackground,
                shadowElevation = 4.dp,
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = item.quality.color,
                    )
                    if (item.ilvl > 0) {
                        Text(
                            "Item Level ${item.ilvl}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.tooltipLabel,
                        )
                    }
                    val stats = buildItemStatLines(item)
                    if (stats.isNotEmpty()) {
                        Text(
                            stats,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                        )
                    }
                    if (item.socketTypes.isNotEmpty()) {
                        Text(
                            "Sockets: ${item.socketTypes.joinToString(", ") { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.tooltipLabel,
                        )
                    }
                    if (option.source.isNotBlank()) {
                        Text(
                            option.source,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.inactive,
                        )
                    }
                }
            }
        },
        delayMillis = 300,
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (item.iconUrl.isNotBlank()) {
                IconImage(url = item.iconUrl, size = 24.dp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = item.quality.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val statParts = mutableListOf<String>()
                if (item.stamina > 0) statParts += "${item.stamina} Stam"
                if (item.agility > 0) statParts += "${item.agility} Agi"
                if (item.defenseRating > 0) statParts += "${item.defenseRating} Def"
                if (item.dodgeRating > 0) statParts += "${item.dodgeRating} Dodge"
                Text(
                    statParts.joinToString(" \u00B7 "),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.statSummary,
                    maxLines = 1,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                if (item.ilvl > 0) {
                    Text("${item.ilvl}", style = MaterialTheme.typography.labelSmall, color = AppColors.tooltipLabel)
                }
                if (option.source.isNotBlank()) {
                    Text(option.source, style = MaterialTheme.typography.labelSmall, color = AppColors.inactive, maxLines = 1)
                }
            }
        }
    }
}

private fun buildItemStatLines(item: Item): String {
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

private fun buildEnchantStatSummary(option: EnchantOption): String {
    val e = option.enchant
    val parts = mutableListOf<String>()
    if (e.stamina != 0) parts += "+${e.stamina} Sta"
    if (e.agility != 0) parts += "+${e.agility} Agi"
    if (e.strength != 0) parts += "+${e.strength} Str"
    if (e.intellect != 0) parts += "+${e.intellect} Int"
    if (e.spirit != 0) parts += "+${e.spirit} Spi"
    if (e.armor != 0) parts += "+${e.armor} Armor"
    if (e.defenseRating != 0) parts += "+${e.defenseRating} Def"
    if (e.dodgeRating != 0) parts += "+${e.dodgeRating} Dodge"
    if (e.resilienceRating != 0) parts += "+${e.resilienceRating} Resil"
    if (e.hitRating != 0) parts += "+${e.hitRating} Hit"
    if (e.expertiseRating != 0) parts += "+${e.expertiseRating} Exp"
    if (e.attackPower != 0) parts += "+${e.attackPower} AP"
    if (e.critRating != 0) parts += "+${e.critRating} Crit"
    if (e.hasteRating != 0) parts += "+${e.hasteRating} Haste"
    val stats = parts.joinToString(", ")
    return if (option.note != null) "$stats  (${option.note})" else stats
}

@Composable
private fun showDiff(label: String, diff: Int) {
    if (diff != 0) {
        val color = if (diff > 0) AppColors.positive else AppColors.negative
        val prefix = if (diff > 0) "+" else ""
        Text(
            "$label: $prefix$diff",
            color = color,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
