package com.hybris.tlv.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.infrastructure.audio.AudioPlayer
import com.hybris.tlv.ui.audio.AudioPlayer
import com.hybris.tlv.ui.command.CommandListener
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.getTranslationState

/**
 * The main composable function that assembles the application UI.
 * Acts as the top-level container for the user-facing elements.
 */
@Composable
internal fun App(
    modifier: Modifier = Modifier,
    vararg compositionValues: ProvidedValue<*>,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = App(*compositionValues) {
    Navigation(
        modifier = modifier,
        navController = navController,
        config = config,
        useCases = useCases
    )

    AudioPlayer(
        navController = navController,
        audioPlayer = audioPlayer
    )

    CommandListener(
        navController = navController,
        audioPlayer = audioPlayer
    )
}

/**
 * A wrapper composable that provides the application theme and composition locals.
 */
@Composable
internal fun App(
    vararg compositionValues: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    val translationState = getTranslationState()
    CompositionLocalProvider(
        *compositionValues,
        LocalTranslationState provides translationState,
    ) {
        AppTheme {
            content()
        }
    }
}
