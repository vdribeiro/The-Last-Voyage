package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.tracker.setCrashHandler
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App() = AppTheme {
    // Setup Navigation
    val navigation = core.navigation
    BackHandler(enabled = true) { navigation.back() }
    val navigationState by navigation.stateFlow.collectAsState()

    // Setup Crash Handler
    LaunchedEffect(key1 = Unit) {
        setCrashHandler { throwable ->
            navigation.navigate(
                screen = Screen.Feedback,
                stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = throwable.stackTraceToString())
            )
        }
    }

    // Setup Audio Player
    val audioPlayer = core.audioPlayer
    val screen = navigationState.screen
    LaunchedEffect(key1 = screen) {
        val playlist = getTracks(screen = screen)
        if (playlist != null) audioPlayer.play(playlist = playlist)
    }
    Register(
        onBackground = { audioPlayer.pause() },
        onForeground = { audioPlayer.resume() },
    )

    // Render Screen
    navigation.Screen(navigationState = navigationState)
}

internal val core: Core by lazy { Core() }
internal const val TAG = "APP"
