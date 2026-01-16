package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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
            navController = rememberNavController(),
            config = dependency.config,
            useCases = dependency.useCases,
            audioPlayer = dependency.audioPlayer,
        )
    }
}
