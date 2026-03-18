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
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Central hub of The Last Voyage application, holding feature flags, dependencies and global listeners.
 */
@ExcludeFromTesting
internal object TLV {

    private const val TAG = "TLV"

    /**
     * Feature flags for production.
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

    private val scope = CoroutineScope(context = SupervisorJob())
    val dependency = MutableStateFlow<Dependency?>(value = null)

    init {
        val flags = FeatureFlags.set { flags }
        Telemetry.engine = Logger()
        Telemetry.info(tag = TAG, message = "App started")
        Telemetry.info(tag = TAG, message = "Features: $flags")

        scope.launch(context = Dispatcher.IO) {
            Telemetry.info(tag = TAG, message = "Initializing dependencies")
            val dependency = createDependency() ?: return@launch
            this@TLV.dependency.update { dependency }

            Telemetry.info(tag = TAG, message = "Registering locale listener")
            observeLocale().collectLatest { languageIso ->
                val translations = dependency.useCases.translation.getTranslations(languageIso = languageIso)
                TranslationCache.set(translations = translations)
            }
        }
    }

    private suspend fun createDependency(): Dependency? {
        val sqlDriver = runCatching {
            createSqlDriver()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create Sql Driver", throwable = it)
        }.getOrDefault(defaultValue = NoOpSqlDriver)

        val httpEngine = runCatching {
            createHttpEngine()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create Http Engine", throwable = it)
        }.getOrDefault(defaultValue = NoOpHttpEngine)

        val audioPlayer = runCatching {
            createAudioPlayer()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create Audio Player", throwable = it)
        }.getOrDefault(defaultValue = AudioPlayer())

        return runCatching {
            Dependency(
                sqlDriver = sqlDriver,
                httpEngine = httpEngine,
                audioPlayer = audioPlayer
            )
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create Dependency Index", throwable = it)
        }.getOrNull()
    }
}
