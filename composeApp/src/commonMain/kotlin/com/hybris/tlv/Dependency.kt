package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.createAudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.ScreenBuilder
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase

/**
 * Dependency index.
 */
internal data class Dependency(
    val sqlDriver: SqlDriver = createSqlDriver(),
    val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    val httpEngine: HttpClientEngine? = null,
    val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    val config: ConfigManager = Config(httpClient = httpClient),
    val useCases: UseCases = Gateways(
        config = config,
        httpClient = httpClient,
        database = database
    ),
    val audioPlayer: AudioPlayer = createAudioPlayer(),
    val navigation: NavigationManager = NavigationManager(initialState = NavigationState()),
    val storeFactory: StoreFactory = StoreFactory(
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    ),
    val screenBuilder: ScreenBuilder = ScreenBuilder(
        config = config,
        storeFactory = storeFactory,
        navigation = navigation
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
