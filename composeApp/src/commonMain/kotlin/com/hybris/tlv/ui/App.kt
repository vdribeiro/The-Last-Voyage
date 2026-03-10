package com.hybris.tlv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.Dependency
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Navigation
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
    val translationMap = getTranslationState()
    val providers = remember(key1 = translationMap, key2 = navController, key3 = dependency) {
        buildList {
            addAll(elements = compositionValues)
            add(element = LocalTranslationState provides translationMap)
            add(element = LocalNavController provides navController)
            if (dependency?.audioPlayer != null) add(element = LocalAudioPlayer provides dependency.audioPlayer)
        }.toTypedArray()
    }

    CompositionLocalProvider(values = providers) {
        AppTheme {
            if (dependency != null) {
                Navigation(
                    modifier = modifier,
                    navController = navController,
                    config = dependency.config,
                    useCases = dependency.useCases
                )
                MusicPlayer(
                    navController = navController,
                    audioPlayer = dependency.audioPlayer,
                )
            } else LoadingScreen()
        }
    }
}

/**
 * A wrapper composable for [androidx.compose.ui.tooling.preview.Preview]s.
 */
@Composable
internal fun Preview(content: @Composable () -> Unit) {
    val translationMap = getTranslationState()
    val providers = remember(key1 = translationMap) {
        buildList {
            add(element = LocalTranslationState provides translationMap)
        }.toTypedArray()
    }

    CompositionLocalProvider(values = providers) {
        AppTheme {
            content()
        }
    }
}
