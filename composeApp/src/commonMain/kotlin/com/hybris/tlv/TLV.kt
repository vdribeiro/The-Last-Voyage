package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * The main object for The Last Voyage application.
 * This object serves as the central hub for the application, holding dependencies, feature flags and a clean entry point for the UI.
 */
@ExcludeFromTesting
internal object TLV {

    private val dependency: Dependency by lazy { Dependency() }

    /**
     * The main composable entry point for the application UI.
     * This function sets up and launches the app's user interface, given a [modifier] to be applied to the root composable.
     */
    @Composable
    fun App(modifier: Modifier = Modifier) {
        App(
            modifier = modifier,
            config = dependency.config,
            useCases = dependency.useCases,
            audioPlayer = dependency.audioPlayer,
        )
    }

    // Feature Flags
    var flag: Flag = Flag(
        reset = false,
        http = true,
        archive = false,
        music = true
    )
}

internal data class Flag(
    /**
     * Flag to enable or disable HTTP client. When false, network calls will fail.
     * This should be set to true for production builds.
     */
    val http: Boolean,
    /**
     * Flag to enable or disable a full data reset before syncing data.
     * This should be set to false for production builds.
     */
    val reset: Boolean,
    /**
     * Flag to enable or disable fetching exoplanet data directly from the NASA archive when syncing data.
     * This should be set to false for production builds.
     */
    val archive: Boolean,
    /**
     * Flag to enable or disable the ambient music in the application.
     * This should be set to true for production builds.
     */
    val music: Boolean
)