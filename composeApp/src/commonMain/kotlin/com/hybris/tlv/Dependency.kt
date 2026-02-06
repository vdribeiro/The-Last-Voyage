package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.audio.createAudioPlayer
import com.hybris.tlv.data.config.Config
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.data.database.DatabaseFactory
import com.hybris.tlv.data.http.HttpClientFactory
import com.hybris.tlv.data.http.createHttpEngine
import com.hybris.tlv.domain.usecase.Gateways
import com.hybris.tlv.domain.usecase.UseCases
import database.AppDatabase

/**
 * Dependency index.
 */
internal class Dependency(
    private val sqlDriver: SqlDriver,
    private val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    private val httpEngine: HttpClientEngine = createHttpEngine(),
    private val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val audioPlayer: AudioPlayer = createAudioPlayer(),
    val config: ConfigManager = Config(httpClient = httpClient),
    val useCases: UseCases = Gateways(
        config = config,
        database = database,
        httpClient = httpClient,
    )
)
