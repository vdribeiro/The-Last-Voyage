package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.command.CommandListener
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.Navigation
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.ObserveTranslations
import com.hybris.tlv.usecase.UseCases

/**
 * The main entry point of the application's UI.
 * This composable sets up the theme, navigation, and audio.
 */
@Composable
internal fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    Navigation(
        modifier = modifier,
        navController = navController,
        config = config,
        useCases = useCases
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    AudioPlayer(
        audioPlayer = audioPlayer,
        destination = navBackStackEntry?.destination
    )

    CommandListener(
        navController = navController,
        audioPlayer = audioPlayer
    )

    ObserveTranslations(
        translation = useCases.translation
    )
}
