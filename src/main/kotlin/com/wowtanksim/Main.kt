package com.wowtanksim

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.wowtanksim.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WoW TBC Feral Druid Tank Simulator",
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
    ) {
        App()
    }
}
