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
 * A composable that handles the navigation listener and displaying a loading indicator or the primary content.
 * @see ScreenContainer
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun Screen(
    store: Store<*, *>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingMinDisplayTimeMillis: Long = 800L,
    loadingText: String = "",
    loadingBackground: Boolean = false,
    loadingProgress: Float? = null,
    onBackClick: (() -> Unit)? = { store.back() },
    onHelpClick: (() -> Unit)? = { store.navigate(screen = Screen.Help) },
    onMusicClick: (() -> Unit)? = { sendCommand(command = Command.ToggleAudio) },
    onFeedbackClick: (() -> Unit)? = { store.navigate(screen = Screen.Feedback()) },
    title: (@Composable () -> Unit)? = null,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    BackHandler { onBackClick?.invoke() }
    val focusManager = LocalFocusManager.current
    ScreenContainer(
        modifier = modifier.pointerInput(key1 = Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        contentAlignment = contentAlignment,
        loading = loading,
        loadingDelayMillis = loadingDelayMillis,
        loadingMinDisplayTimeMillis = loadingMinDisplayTimeMillis,
        loadingText = loadingText,
        loadingBackground = loadingBackground,
        loadingProgress = loadingProgress,
        onBackClick = onBackClick,
        onHelpClick = onHelpClick,
        onMusicClick = onMusicClick,
        onFeedbackClick = onFeedbackClick,
        title = title,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content
    )
}