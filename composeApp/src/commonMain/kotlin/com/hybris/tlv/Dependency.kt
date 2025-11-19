package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import androidx.annotation.VisibleForTesting
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.createAudioPlayer
import com.hybris.tlv.ui.navigation.ScreenBuilder
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase

/**
 * Dependency index.
 */
internal class Dependency(
    @get:VisibleForTesting internal val sqlDriver: SqlDriver = createSqlDriver(),
    private val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    private val httpEngine: HttpClientEngine? = null,
    private val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val config: ConfigManager = Config(httpClient = httpClient),
    @get:VisibleForTesting internal val useCases: UseCases = Gateways(
        config = config,
        httpClient = httpClient,
        database = database
    ),
    val audioPlayer: AudioPlayer = createAudioPlayer(),
    @get:VisibleForTesting internal val storeFactory: StoreFactory = StoreFactory(
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    ),
)

internal val dependency: Dependency by lazy { Dependency() }
