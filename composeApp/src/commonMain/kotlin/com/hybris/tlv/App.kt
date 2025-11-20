package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

@Composable
internal fun App(
    modifier: Modifier = Modifier,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    val navController = rememberNavController()
    Navigation(
        modifier = modifier,
        navController = navController,
        config = config,
        useCases = useCases,
    )

    val screen = navController.currentDestination as? Screen
    AudioPlayer(
        audioPlayer = audioPlayer,
        screen = screen
    )
}
