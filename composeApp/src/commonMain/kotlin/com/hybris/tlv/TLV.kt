package com.hybris.tlv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.audio.createAudioPlayer
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.observeLocale
import com.hybris.tlv.core.platform.isDebug
import com.hybris.tlv.core.telemetry.Logger
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.NoOpSqlDriver
import com.hybris.tlv.data.database.createSqlDriver
import com.hybris.tlv.data.http.NoOpHttpEngine
import com.hybris.tlv.data.http.createHttpEngine
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.flag.Flags
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.TranslationGateway.Companion.loadAllTranslationsFromJsonResource
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Central hub of The Last Voyage application.
 * This singleton is responsible for the "Cold Start" of the application logic. It performs the following critical startup tasks:
 * 1. **Feature Flag Initialization:** Configures the initial state of [FeatureFlags].
 * 2. **Telemetry Setup:** Injects the [Logger] engine into the global [Telemetry] hub.
 * 3. **Dependency Injection:** Asynchronously instantiates the [Dependency] graph, including platform-specific drivers for Database and Networking.
 * 4. **Localization:** Seeds the initial [TranslationCache] and registers a reactive listener to handle system-level locale changes.
 *
 * ### Lifecycle:
 * The initialization begins immediately upon the first access to this object.
 * All heavy operations are offloaded to a [SupervisorJob] on [Dispatcher.IO] to keep the app responsive during bootstrap.
 */
@ExcludeFromTesting
internal object TLV {

    private const val TAG = "TLV"

    /**
     * Production-ready feature flags.
     */
    private val flags = Flags(
        devMode = isDebug,
        console = true,
        reset = false,
        http = true,
        archive = false,
        music = true,
        engines = false
    )

    /**
     * Global scope for application-wide background tasks.
     */
    private val scope = CoroutineScope(context = SupervisorJob())

    /**
     * Reactive holder for the [Dependency] index.
     * Modules should observe this flow to ensure they only interact with dependencies after the bootstrap is complete.
     */
    val dependency = MutableStateFlow<Dependency?>(value = null)

    init {
        val flags = FeatureFlags.set { flags }
        Telemetry.engine = Logger()
        Telemetry.info(tag = TAG, message = "App started")
        Telemetry.info(tag = TAG, message = "Features: $flags")

        scope.launch(context = Dispatcher.IO) {
            Telemetry.info(tag = TAG, message = "Loading translations")
            val translations: List<Translation> = loadAllTranslationsFromJsonResource()
            TranslationCache.set(translations = translations)

            Telemetry.info(tag = TAG, message = "Initializing dependencies")
            val dependency = createDependency() ?: return@launch
            this@TLV.dependency.update { dependency }

            Telemetry.info(tag = TAG, message = "Registering locale listener")
            observeLocale(translation = dependency.useCases.translation)
        }
    }

    /**
     * Orchestrates the creation of platform-dependent engines.
     * Uses a resilient "Best Effort" pattern: if a platform driver fails to initialize (e.g., storage is corrupted), it falls back to a "No-Op" implementation to prevent a total application crash.
     *
     * @return A fully populated [Dependency] object, or null if the core index failed.
     */
    private suspend fun createDependency(): Dependency? {
        val sqlDriver = runCatching {
            createSqlDriver()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create the Sql Driver", throwable = it)
        }.getOrDefault(defaultValue = NoOpSqlDriver)

        val httpEngine = runCatching {
            createHttpEngine()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create the Http Engine", throwable = it)
        }.getOrDefault(defaultValue = NoOpHttpEngine)

        val audioPlayer = runCatching {
            createAudioPlayer()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create the Audio Player", throwable = it)
        }.getOrDefault(defaultValue = AudioPlayer())

        return runCatching {
            Dependency(
                sqlDriver = sqlDriver,
                httpEngine = httpEngine,
                audioPlayer = audioPlayer
            )
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create the Dependency Index", throwable = it)
        }.getOrNull()
    }

    /**
     * Subscribes to system locale changes and refreshes the [TranslationCache].
     *
     * @param translation The use-case container used to fetch updated strings for the new locale.
     */
    private suspend fun observeLocale(translation: TranslationUseCases) {
        observeLocale().collectLatest { languageIso ->
            val translations = translation.getTranslations(languageIso = languageIso).ifEmpty {
                loadAllTranslationsFromJsonResource()
            }
            TranslationCache.set(translations = translations)
        }
    }
}