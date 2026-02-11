package com.hybris.tlv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.observeLocale
import com.hybris.tlv.core.platform.isDebug
import com.hybris.tlv.core.telemetry.Logger
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.createSqlDriver
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.flag.Flags
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App
import com.hybris.tlv.ui.theme.component.container.Screen

/**
 * The main object for The Last Voyage application.
 * Serves as the central hub, holding dependencies and providing a clean entry point for the UI.
 */
@ExcludeFromTesting
internal object TLV {

    private const val TAG = "TLV"

    /**
     * Feature flags for production.
     */
    private val flags = Flags(
        devMode = isDebug,
        reset = false,
        http = true,
        archive = false,
        music = true
    )

    private val scope = CoroutineScope(context = SupervisorJob())
    private val dependency = MutableStateFlow<Dependency?>(value = null)

    init {
        val flags = FeatureFlags.set { flags }
        Telemetry.engine = Logger()

        Telemetry.info(tag = TAG, message = "App started")
        Telemetry.info(tag = TAG, message = "Features: $flags")

        Telemetry.info(tag = TAG, message = "Initializing dependencies")
        scope.launch(context = Dispatcher.IO) {
            val dependency = Dependency(sqlDriver = createSqlDriver())
            this@TLV.dependency.update { dependency }

            Telemetry.info(tag = TAG, message = "Registering locale listener")
            observeLocale().collectLatest { languageIso ->
                val translationUseCases = dependency.useCases.translation
                TranslationCache.set(translations = translationUseCases.getTranslations(languageIso = languageIso))
            }
        }
    }

    /**
     * The main composable entry point for the application UI.
     * This function sets up and launches the app's user interface, given a [modifier] to be applied to the root composable,
     * and [compositionValues] to be passed down the composition tree.
     */
    @Composable
    fun App(
        modifier: Modifier,
        vararg compositionValues: ProvidedValue<*>
    ) {
        val dependency by dependency.collectAsState()
        val config = dependency?.config
        val useCases = dependency?.useCases
        val audioPlayer = dependency?.audioPlayer
        if (config == null || useCases == null || audioPlayer == null) App(*compositionValues) {
            Screen(
                contentAlignment = Alignment.Center,
                loading = true,
                loadingDelayMillis = 0L,
                loadingBackground = true
            )
        } else App(
            modifier = modifier,
            compositionValues = compositionValues,
            navController = rememberNavController(),
            config = config,
            useCases = useCases,
            audioPlayer = audioPlayer,
        )
    }
}
