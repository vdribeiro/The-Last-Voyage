package com.hybris.tlv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.hybris.tlv.core.audio.createAudioPlayer
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.observeLocale
import com.hybris.tlv.core.platform.isDebug
import com.hybris.tlv.core.telemetry.Logger
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.createSqlDriver
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
            val dependency = Dependency(
                sqlDriver = createSqlDriver(),
                httpEngine = createHttpEngine(),
                audioPlayer = createAudioPlayer()
            )
            this@TLV.dependency.update { dependency }

            Telemetry.info(tag = TAG, message = "Registering locale listener")
            observeLocale().collectLatest { languageIso ->
                TranslationCache.set(translations = dependency.useCases.translation.getTranslations(languageIso = languageIso))
            }
        }
    }
}
