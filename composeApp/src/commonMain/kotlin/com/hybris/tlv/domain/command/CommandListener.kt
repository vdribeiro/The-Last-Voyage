package com.hybris.tlv.domain.command

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.hybris.tlv.infrastructure.audio.AudioPlayer
import com.hybris.tlv.ui.navigation.back
import com.hybris.tlv.ui.navigation.navigate

/**
 * Listens for commands and performs the corresponding actions.
 */
@Composable
internal fun CommandListener(
    navController: NavHostController,
    audioPlayer: AudioPlayer,
) {
    LaunchedEffect(key1 = Unit) {
        receiveCommand { command ->
            when (command) {
                is Command.Navigate -> navController.navigate(screen = command.screen)
                Command.Back -> navController.back()
                Command.ToggleAudio -> audioPlayer.action(action = AudioPlayer.Action.Toggle)
            }
        }
    }
}
