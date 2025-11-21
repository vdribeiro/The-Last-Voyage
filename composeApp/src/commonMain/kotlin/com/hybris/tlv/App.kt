package com.hybris.tlv

import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Action
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.actionChannel
import com.hybris.tlv.ui.navigation.navigate
import com.hybris.tlv.ui.navigation.navigationChannel
import com.hybris.tlv.ui.navigation.toScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val screen = remember(key1 = navBackStackEntry) { navBackStackEntry?.toScreen() }
    val haptics = LocalHapticFeedback.current

    Navigation(
        modifier = modifier,
        navController = navController,
        config = config,
        useCases = useCases
    )

    LaunchedEffect(key1 = navBackStackEntry) {
        navigationChannel.receiveAsFlow().collect { screen -> navController.navigate(screen = screen) }
    }

    LaunchedEffect(key1 = navBackStackEntry) {
        actionChannel.receiveAsFlow().collect { action ->
            when (action) {
                Action.Back -> navController.popBackStack()
                Action.ToggleAudio -> audioPlayer.action(action = AudioPlayer.Action.Toggle)
                is Action.Cheats -> withContext(context = Dispatcher.IO) {
                    haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Reject)
                    //TODO
                }
            }
        }
    }

    AudioPlayer(
        audioPlayer = audioPlayer,
        screen = screen
    )
}
