package com.hybris.tlv.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.Dependency
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.telemetry.Console
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.screen.LoadingScreen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
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
    val providers = remember(
        compositionValues,
        translationMap,
        navController,
        dependency
    ) {
        buildList {
            addAll(elements = compositionValues)
            add(element = LocalTranslationState provides translationMap)
            add(element = LocalNavController provides navController)
            if (dependency != null) {
                add(element = LocalAudioPlayer provides dependency.audioPlayer)
            }
        }.toTypedArray()
    }

    CompositionLocalProvider(values = providers) {
        AppTheme {
            if (dependency != null) App(
                modifier = modifier,
                navController = navController,
                storeFactory = dependency.storeFactory,
                audioPlayer = dependency.audioPlayer
            ) else Loading()
        }
    }
}

@Composable
private fun Loading() {
    var logs: String? by remember { mutableStateOf(value = null) }
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            logs = Console.getSnapshot().joinToString(separator = "\n").ifBlank { null }
            delay(timeMillis = 500)
        }
    }
    LoadingScreen(
        logs = logs,
        sendFeedback = { Telemetry.feedback(message = it) }
    )
}

@Composable
private fun App(
    modifier: Modifier,
    navController: NavHostController,
    storeFactory: StoreFactory,
    audioPlayer: AudioPlayer
) {
    Navigation(
        modifier = modifier,
        navController = navController,
        storeFactory = storeFactory
    )
    MusicPlayer(
        navController = navController,
        audioPlayer = audioPlayer,
    )
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

@Preview
@Composable
private fun AppLoadingPreview() = Preview {
    App()
}

@Preview
@Composable
private fun AppPreview() = Preview {
    App(
        navController = rememberNavController(),
        dependency = Dependency()
    )
}
