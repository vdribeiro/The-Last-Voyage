package com.hybris.tlv

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.data.config.Config
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.data.database.DatabaseFactory
import com.hybris.tlv.data.database.createSqlDriver
import com.hybris.tlv.data.http.HttpClientFactory
import com.hybris.tlv.domain.usecase.Gateways
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.infrastructure.audio.AudioPlayer
import com.hybris.tlv.infrastructure.audio.createAudioPlayer
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.test.VisibleOnlyForTesting
import com.hybris.tlv.ui.audio.AudioPlayer
import com.hybris.tlv.ui.command.CommandListener
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.getTranslationState
import database.AppDatabase

/**
 * The main object for The Last Voyage application.
 * Serves as the central hub, holding dependencies and a clean entry point for the UI.
 */
@ExcludeFromTesting
internal object TLV {

    private val dependency: Dependency by lazy { Dependency() }

    // TODO - Refresh translations on locale changes.

    /**
     * The main composable entry point for the application UI.
     * This function sets up and launches the app's user interface, given a [modifier] to be applied to the root composable,
     * and [compositionValues] to be applied through [androidx.compose.runtime.CompositionLocalProvider].
     */
    @Composable
    fun App(
        modifier: Modifier,
        vararg compositionValues: ProvidedValue<*>
    ) = App(
        modifier = modifier,
        compositionValues = compositionValues,
        navController = rememberNavController(),
        config = dependency.config,
        useCases = dependency.useCases,
        audioPlayer = dependency.audioPlayer,
    )
}

/**
 * The main entry point of the application's UI.
 * Sets up the theme, navigation, audio and commands.
 */
@VisibleOnlyForTesting
@Composable
internal fun App(
    modifier: Modifier = Modifier,
    vararg compositionValues: ProvidedValue<*>,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = App(*compositionValues) {
    Navigation(
        modifier = modifier,
        navController = navController,
        config = config,
        useCases = useCases
    )

    AudioPlayer(
        navController = navController,
        audioPlayer = audioPlayer
    )

    CommandListener(
        navController = navController,
        audioPlayer = audioPlayer
    )
}

@VisibleOnlyForTesting
@Composable
internal fun App(
    vararg compositionValues: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    val translationState = getTranslationState()
    CompositionLocalProvider(
        *compositionValues,
        LocalTranslationState provides translationState,
    ) {
        AppTheme {
            content()
        }
    }
}

/**
 * Dependency index.
 */
@VisibleOnlyForTesting
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
