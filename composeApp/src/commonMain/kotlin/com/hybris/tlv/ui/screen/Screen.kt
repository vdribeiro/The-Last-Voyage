package com.hybris.tlv.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.navigation.NavigationHandler
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.theme.component.container.Screen as ScreenContainer

/**
 * A composable that handles the navigation listener and displaying a loading indicator or the primary content.
 * @see ScreenContainer
 */
@Composable
internal fun Screen(
    store: Store<*, *>,
    modifier: Modifier = Modifier,
    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
    contentAlignment: Alignment = Alignment.TopStart,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingMinDisplayTimeMillis: Long = 800L,
    loadingText: String = "",
    loadingBackground: Boolean = false,
    loadingProgress: Float? = null,
    onBackClick: (() -> Unit)? = { store.navigateBack() },
    onHelpClick: (() -> Unit)? = { store.navigate(screen = Screen.Help) },
    onMusicClick: (() -> Unit)? = { audioPlayer.action(action = AudioPlayer.Action.Toggle) },
    onFeedbackClick: (() -> Unit)? = { store.navigate(screen = Screen.Feedback()) },
    title: (@Composable () -> Unit)? = null,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    NavigationHandler(onBack = onBackClick)
    ScreenContainer(
        modifier = modifier,
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