package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App

/**
 * The main object for The Last Voyage application.
 * Serves as the central hub, holding dependencies and providing a clean entry point for the UI.
 */
@ExcludeFromTesting
internal object TLV {

    private const val TAG = "TLV"

    private val dependency: Dependency by lazy { Dependency() }

    init {
        Telemetry.info(tag = TAG, message = "Registering listeners")
        registerTranslationListener()
    }

    /**
     * Registers a listener to observe system locale changes to refresh the translation cache.
     */
    private fun registerTranslationListener() {
        TranslationCache.registerListener {
            dependency.useCases.translation.getTranslations()
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
    ) = App(
        modifier = modifier,
        compositionValues = compositionValues,
        navController = rememberNavController(),
        config = dependency.config,
        useCases = dependency.useCases,
        audioPlayer = dependency.audioPlayer,
    )
}
