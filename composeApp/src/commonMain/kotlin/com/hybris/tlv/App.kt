package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.receiveCommand
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.Navigation
import com.hybris.tlv.navigation.back
import com.hybris.tlv.navigation.navigate
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

/**
 * The main entry point of the application's UI.
 * This composable sets up the theme, navigation, and audio.
 */
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
        useCases = useCases
    )

    LaunchedEffect(key1 = Unit) {
        receiveCommand { command ->
            when (command) {
                is Command.Navigate -> navController.navigate(screen = command.screen, restore = command.restore)
                Command.Back -> navController.back()
                Command.ToggleAudio -> audioPlayer.action(action = AudioPlayer.Action.Toggle)
            }
        }
    }

    if (flag.music) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        AudioPlayer(
            audioPlayer = audioPlayer,
            destination = navBackStackEntry?.destination
        )
    }
}
