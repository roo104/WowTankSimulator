package com.wowtanksim.service

import com.wowtanksim.model.EquipSlot
import com.wowtanksim.model.ItemQuality
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class WishListEntry(
    val itemId: Int,
    val itemName: String,
    val slot: EquipSlot,
    val iconUrl: String = "",
    val quality: ItemQuality = ItemQuality.COMMON,
    val source: String = "",
)

@Serializable
data class WishListData(
    val lists: Map<String, List<WishListEntry>> = mapOf("Default" to emptyList())
)

object WishListService {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val file: File
        get() {
            val dir = File(System.getProperty("user.home"), ".wowtanksim")
            if (!dir.exists()) dir.mkdirs()
            return File(dir, "wishlist.json")
        }

    fun load(): WishListData {
        return try {
            if (file.exists()) {
                val text = file.readText()
                try {
                    // Try new format first
                    json.decodeFromString<WishListData>(text)
                } catch (_: Exception) {
                    // Migration: old format was a plain list
                    val oldEntries = json.decodeFromString<List<WishListEntry>>(text)
                    val migrated = WishListData(lists = mapOf("Default" to oldEntries))
                    save(migrated)
                    migrated
                }
            } else {
                WishListData()
            }
        } catch (e: Exception) {
            WishListData()
        }
    }

    fun save(data: WishListData) {
        try {
            file.writeText(json.encodeToString(WishListData.serializer(), data))
        } catch (_: Exception) {
            // silently fail
        }
    }
}
