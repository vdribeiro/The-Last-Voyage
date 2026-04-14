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
 * A feature-aware layout wrapper that integrates Navigation and Audio services with the UI scaffold.
 * This composable acts as the primary entry point for screen implementations.
 * It provides default navigation behaviors, while also handling hardware-specific interactions like the physical back button and mouse side-buttons.
 *
 * @param modifier Standard [Modifier] applied to the root container.
 * @param navController The [NavHostController] used for screen transitions.
 * @param audioPlayer The [AudioPlayer] instance for music control.
 * @param contentAlignment The alignment of the main content within the screen's body.
 * @param loading Whether the screen is currently in a loading state.
 * @param loadingDelayMillis The grace period in milliseconds to wait before showing the loader.
 * @param loadingMinDisplayTimeMillis The minimum time in milliseconds the loader stays visible once shown.
 * @param loadingText Optional status text displayed beneath the loading animation.
 * @param loadingBackground If true, applies a distinct background to the loading overlay.
 * @param loadingProgress Optional deterministic progress (0.0 to 1.0) for the loading indicator.
 * @param onBackClick Lambda for back navigation.
 * @param onHelpClick Lambda for the help icon.
 * @param onMusicClick Lambda for the music icon.
 * @param onFeedbackClick Lambda for the feedback icon.
 * @param title Optional center-aligned composable slot for the TopBar.
 * @param topBar Optional slot for UI elements placed immediately below the TopBar.
 * @param bottomBar Optional slot for UI elements pinned to the screen bottom (handles navigation padding).
 * @param snackbarHost Container for displaying transient Snackbar notifications.
 * @param content The primary UI content to display when not loading.
 *
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
