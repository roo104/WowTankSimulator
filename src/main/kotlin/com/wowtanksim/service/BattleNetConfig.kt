package com.wowtanksim.service

import com.wowtanksim.util.DebugLog
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class BattleNetConfig(
    val clientId: String,
    val clientSecret: String,
) {
    companion object {
        private val configDir = File(System.getProperty("user.home"), ".wowtanksim")
        private val configFile = File(configDir, "config.json")
        private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

        fun load(): Result<BattleNetConfig> {
            if (!configFile.exists()) {
                configDir.mkdirs()
                val placeholder = """
                    {
                        "clientId": "YOUR_CLIENT_ID",
                        "clientSecret": "YOUR_CLIENT_SECRET"
                    }
                """.trimIndent()
                configFile.writeText(placeholder)
                return Result.failure(
                    RuntimeException(
                        "Battle.net API credentials not configured.\n" +
                            "Edit ${configFile.absolutePath} with your client ID and secret.\n" +
                            "Create credentials at https://develop.battle.net/access/clients"
                    )
                )
            }

            return try {
                val config = json.decodeFromString<BattleNetConfig>(configFile.readText())
                if (config.clientId.isBlank() || config.clientId == "YOUR_CLIENT_ID") {
                    Result.failure(
                        RuntimeException(
                            "Battle.net API credentials are placeholder values.\n" +
                                "Edit ${configFile.absolutePath} with your client ID and secret.\n" +
                                "Create credentials at https://develop.battle.net/access/clients"
                        )
                    )
                } else {
                    Result.success(config)
                }
            } catch (e: Exception) {
                DebugLog.error("Failed to parse ${configFile.absolutePath}", e)
                Result.failure(
                    RuntimeException("Failed to parse ${configFile.absolutePath}: ${e.message}")
                )
            }
        }
    }
}
