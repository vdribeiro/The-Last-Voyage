package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.createAudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase

/**
 * Dependency index.
 */
internal data class Dependency(
    val dispatcher: Dispatcher = Dispatchers(),
    val sqlDriver: SqlDriver = createSqlDriver(),
    val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    val httpEngine: HttpClientEngine? = null,
    val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val config: ConfigManager = Config(httpClient = httpClient),
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        config = config,
        httpClient = httpClient,
        database = database
    ),
    val audioPlayer: AudioPlayer = createAudioPlayer(),
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases,
        initialState = NavigationState()
    )
) {
    init {
        Telemetry.info(tag = TAG, message = "Dependencies setup complete")
    }

    companion object {
        private const val TAG = "Dependency"
    }
}

internal val dependency: Dependency by lazy { Dependency() }
