package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.config.Configs
import com.hybris.tlv.config.Preferences
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.createAudioPlayer
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase

/**
 * Dependency index.
 */
internal class Dependency(
    private val sqlDriver: SqlDriver = createSqlDriver(),
    private val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    private val httpEngine: HttpClientEngine? = null,
    private val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val config: ConfigManager = Config(httpClient = httpClient),
    val useCases: UseCases = Gateways(
        config = config,
        httpClient = httpClient,
        database = database
    ),
    val audioPlayer: AudioPlayer = createAudioPlayer()
) {
    suspend fun reset() {
        sqlDriver.clearDatabase()
        config.setPreferences { Preferences() }
        config.setConfigs { Configs() }
    }

    companion object {
        /**
         * Enable or disable HTTP client.
         */
        const val HTTP = true
        /**
         * Enable or disable getting exoplanet data from the NASA archive (only works if HTTP is enabled).
         */
        const val ARCHIVE = false
        /**
         * Enable or disable ambient music.
         */
        const val MUSIC = true
    }
}
