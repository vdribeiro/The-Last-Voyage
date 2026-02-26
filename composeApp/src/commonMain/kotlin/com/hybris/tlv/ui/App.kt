package com.hybris.tlv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.hybris.tlv.Dependency
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.navigationEventDispatcherOwner
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.component.container.LoadingScreen
import com.hybris.tlv.ui.theme.getTranslationState
import com.hybris.tlv.ui.audio.AudioPlayer as MusicPlayer

/**
 * The main composable function that assembles the application UI.
 * Acts as the top-level container for the user-facing elements.
 */
@Composable
internal fun App(
    modifier: Modifier = Modifier,
    compositionValues: List<ProvidedValue<*>> = emptyList(),
    navController: NavHostController = rememberNavController(),
    dependency: Dependency? = null
) {
    val compositionValues = compositionValues.toMutableList().apply {
        add(element = LocalTranslationState provides getTranslationState())
        add(element = LocalNavController provides navController)
        add(element = LocalNavigationEventDispatcherOwner provides navigationEventDispatcherOwner)
        dependency?.audioPlayer?.let { add(element = LocalAudioPlayer provides it) }
    }
    CompositionLocalProvider(*compositionValues.toTypedArray()) {
        AppTheme {
            if (dependency == null) LoadingScreen() else {
                Navigation(
                    modifier = modifier,
                    config = dependency.config,
                    useCases = dependency.useCases
                )
                MusicPlayer()
            }
        }
    }
}

/**
 * A wrapper composable for [androidx.compose.ui.tooling.preview.Preview]s.
 */
@Composable
internal fun Preview(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalTranslationState provides getTranslationState()) {
        AppTheme {
            content()
        }
    }
}
