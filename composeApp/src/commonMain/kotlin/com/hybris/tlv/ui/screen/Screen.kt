package com.hybris.tlv.ui.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.audio.LocalAudioPlayer
import com.hybris.tlv.ui.navigation.LocalNavController
import com.hybris.tlv.ui.navigation.NavigationHandler
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.back
import com.hybris.tlv.ui.navigation.navigate
import com.hybris.tlv.ui.theme.modifier.MouseClick
import com.hybris.tlv.ui.theme.modifier.onMouseClick
import com.hybris.tlv.ui.theme.component.container.Screen as ScreenContainer

/**
 * A composable that handles the navigation listener and displaying a loading indicator or the primary content.
 * @see ScreenContainer
 */
@Composable
internal fun Screen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = LocalNavController.current,
    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
    contentAlignment: Alignment = Alignment.TopStart,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingMinDisplayTimeMillis: Long = 800L,
    loadingText: String = "",
    loadingBackground: Boolean = false,
    loadingProgress: Float? = null,
    onLoadingFinished: ((Float) -> Unit)? = null,
    onBackClick: (() -> Unit)? = { navController?.back() },
    onHelpClick: (() -> Unit)? = { navController?.navigate(screen = Screen.Help) },
    onMusicClick: (() -> Unit)? = { audioPlayer.action(action = AudioPlayer.Action.Toggle) },
    onFeedbackClick: (() -> Unit)? = { navController?.navigate(screen = Screen.Feedback(tag = null, message = null)) },
    title: (@Composable () -> Unit)? = null,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    NavigationHandler(onBack = onBackClick)
    ScreenContainer(
        modifier = modifier.onMouseClick(mouseClicks = listOf(element = MouseClick.BACK)) { onBackClick?.invoke() },
        contentAlignment = contentAlignment,
        loading = loading,
        loadingDelayMillis = loadingDelayMillis,
        loadingMinDisplayTimeMillis = loadingMinDisplayTimeMillis,
        loadingText = loadingText,
        loadingBackground = loadingBackground,
        loadingProgress = loadingProgress,
        onLoadingFinished = onLoadingFinished,
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

@Preview
@Composable
private fun ScreenPreview() = Preview {
    Screen()
}
