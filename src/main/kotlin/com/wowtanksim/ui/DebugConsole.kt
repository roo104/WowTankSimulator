package com.wowtanksim.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wowtanksim.util.DebugLog

@Composable
fun DebugConsole(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val entries = DebugLog.entries
    val scrollState = rememberScrollState()

    LaunchedEffect(entries.size) {
        if (entries.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Debug Console \u25BC" else "Debug Console \u25B6")
            }
            if (entries.isNotEmpty()) {
                val errorCount = entries.count { it.level == DebugLog.Level.ERROR }
                if (errorCount > 0) {
                    Text(
                        "$errorCount error(s)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { DebugLog.clear() }) {
                    Text("Clear", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        if (expanded) {
            Surface(
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                color = AppColors.debugBackground,
                shape = MaterialTheme.shapes.small,
                tonalElevation = 2.dp,
            ) {
                SelectionContainer {
                    if (entries.isEmpty()) {
                        Box(Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                "No log entries.",
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState).padding(8.dp),
                        ) {
                            entries.forEach { entry ->
                                Text(
                                    text = entry.message,
                                    color = when (entry.level) {
                                        DebugLog.Level.INFO -> AppColors.debugInfo
                                        DebugLog.Level.ERROR -> AppColors.debugError
                                    },
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 1.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
