package com.hybris.tlv.dependency

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.audio.createAudioPlayer
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.http.HttpClientFactory
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
        database = database,
        httpClient = httpClient,
    ),
    val audioPlayer: AudioPlayer = createAudioPlayer()
)
