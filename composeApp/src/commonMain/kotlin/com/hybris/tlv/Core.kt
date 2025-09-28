package com.hybris.tlv

import androidx.annotation.VisibleForTesting
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.DatabaseFactory
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

internal data class Core(
    @get:VisibleForTesting internal val dispatcher: Dispatcher = Dispatchers(),
    @get:VisibleForTesting internal val sqlDriver: SqlDriver = createSqlDriver(),
    @get:VisibleForTesting internal val database: AppDatabase = DatabaseFactory(driver = sqlDriver).database,
    @get:VisibleForTesting internal val httpEngine: HttpClientEngine? = null,
    @get:VisibleForTesting internal val httpClient: HttpClient = HttpClientFactory(engine = httpEngine).httpClient,
    @get:VisibleForTesting internal val config: ConfigManager = Config(httpClient = httpClient),
    @get:VisibleForTesting internal val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        config = config,
        httpClient = httpClient,
        database = database
    ),
    val audioPlayer: AudioPlayer = AudioPlayer(),
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    )
)
