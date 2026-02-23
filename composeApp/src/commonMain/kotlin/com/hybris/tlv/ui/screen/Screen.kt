package com.hybris.tlv.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.hybris.tlv.ui.command.Command
import com.hybris.tlv.ui.command.sendCommand
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.theme.component.container.Screen as ScreenContainer

/**
 * A composable that handles displaying a loading indicator or the primary content.
 * @see ScreenContainer
 */
@OptIn(ExperimentalComposeUiApi::class)
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
    BackHandler { if (back) store.back() }
    val focusManager = LocalFocusManager.current
    ScreenContainer(
        modifier = modifier.pointerInput(key1 = Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        contentAlignment = contentAlignment,
        loading = loading,
        loadingDelayMillis = loadingDelayMillis,
        loadingText = loadingText,
        loadingBackground = loadingBackground,
        loadingProgress = loadingProgress,
        onBackClick = if (back) {
            { store.back() }
        } else null,
        onHelpClick = if (help) {
            { store.navigate(screen = Screen.Help) }
        } else null,
        onMusicClick = if (music) {
            { sendCommand(command = Command.ToggleAudio) }
        } else null,
        onFeedbackClick = if (feedback) {
            { store.navigate(screen = Screen.Feedback()) }
        } else null,
        title = title,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content
    )
}