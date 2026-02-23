package com.hybris.tlv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.command.CommandListener
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.getTranslationState
import  com.hybris.tlv.ui.audio.AudioPlayer as MusicPlayer

/**
 * The main composable function that assembles the application UI.
 * Acts as the top-level container for the user-facing elements.
 */
@Composable
internal fun App(
    modifier: Modifier,
    compositionValues: List<ProvidedValue<*>>,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) {
    val compositionValues = compositionValues + listOf(
        LocalTranslationState provides getTranslationState(),
        LocalNavController provides navController,
        LocalAudioPlayer provides audioPlayer,
    )
    CompositionLocalProvider(*compositionValues.toTypedArray()) {
        AppTheme {
            Navigation(
                modifier = modifier,
                config = config,
                useCases = useCases
            )
            MusicPlayer()
            CommandListener()
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
