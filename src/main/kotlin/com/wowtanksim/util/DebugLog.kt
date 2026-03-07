package com.wowtanksim.util

import androidx.compose.runtime.mutableStateListOf

object DebugLog {
    val entries = mutableStateListOf<Entry>()

    data class Entry(val level: Level, val message: String)

    enum class Level { INFO, ERROR }

    fun info(message: String) {
        entries.add(Entry(Level.INFO, message))
    }

    fun error(message: String) {
        entries.add(Entry(Level.ERROR, message))
    }

    fun error(message: String, throwable: Throwable) {
        entries.add(Entry(Level.ERROR, "$message: ${throwable::class.simpleName} - ${throwable.message}"))
    }

    fun clear() {
        entries.clear()
    }
}
