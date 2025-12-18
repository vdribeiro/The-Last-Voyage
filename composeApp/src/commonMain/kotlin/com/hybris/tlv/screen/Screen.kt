package com.hybris.tlv.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.sendCommand
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.theme.component.container.Screen as ScreenContainer

@Composable
internal fun <State, Action> Screen(
    store: Store<State, Action>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingText: String = "",
    loadingBackground: Boolean = false,
    loadingProgress: Float? = null,
    back: Boolean = true,
    help: Boolean = true,
    music: Boolean = true,
    feedback: Boolean = true,
    title: (@Composable () -> Unit)? = null,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    ScreenContainer(
        modifier = modifier,
        contentAlignment = contentAlignment,
        loading = loading,
        loadingDelayMillis = loadingDelayMillis,
        loadingText = loadingText,
        loadingBackground = loadingBackground,
        loadingProgress = loadingProgress,
        onBackClick = { if (back) store.back() },
        onHelpClick = { if (help) sendCommand(command = Command.Navigate(screen = Screen.Help)) },
        onMusicClick = { if (music) sendCommand(command = Command.ToggleAudio) },
        onFeedbackClick = { if (feedback) sendCommand(command = Command.Navigate(screen = Screen.Feedback())) },
        title = title,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content
    )
}