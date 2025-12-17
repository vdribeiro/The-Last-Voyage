package com.hybris.tlv

import kotlinx.coroutines.flow.receiveAsFlow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Command
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.commandChannel
import com.hybris.tlv.ui.navigation.navigate
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
        commandChannel.receiveAsFlow().collect { command ->
            when (command) {
                is Command.Navigate -> navController.navigate(screen = command.screen, command.restore)
                Command.Back -> navController.popBackStack()
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
