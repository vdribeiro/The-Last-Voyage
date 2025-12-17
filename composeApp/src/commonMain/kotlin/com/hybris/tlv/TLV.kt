package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.TLV.HTTP
import com.hybris.tlv.dependency.Dependency

/**
 * The main object for The Last Voyage application.
 * This object serves as the central hub for the application, holding dependencies, feature flags and a clean entry point for the UI.
 */
internal object TLV {

    private val dependency: Dependency = Dependency()

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

    // Development Flags
    /**
     * Flag to trigger a full data reset on startup.
     * This should be set to false for production builds.
     */
    const val RESET = false
    /**
     * Flag to enable or disable all HTTP client network requests.
     * When false, only local data is used and no network calls will be made by the application.
     * This should be set to true for production builds.
     */
    const val HTTP = true
    /**
     * Flag to enable or disable fetching exoplanet data from the NASA archive.
     * This is only effective if [HTTP] is also true.
     * This should be set to false for production builds.
     */
    const val ARCHIVE = false
    /**
     * Flag to enable or disable the ambient music in the application.
     * This should be set to true for production builds.
     */
    const val MUSIC = true
}
