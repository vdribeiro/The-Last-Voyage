package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.progress.showLoading
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.topbar.TopBar

/**
 * The foundational UI Scaffold that provides a standardized structure for all screens, ensuring consistent handling of system bars and loading states.
 * It is designed to prevent UI flickering during data loading by employing a two-part strategy:
 * - An initial grace period [loadingDelayMillis] to avoid showing the loader for very fast operations.
 * - A minimum display time [loadingMinDisplayTimeMillis] to ensure that if the loader does appear, it remains on screen long enough to avoid the same problem.
 *
 * ### Layout Structure:
 * - **TopBar:** Automatically handles [statusBarsPadding].
 * - **Content:** A [Box] that toggles between the [AppLogo] (loader) and screen [content].
 * - **BottomBar:** Automatically handles [navigationBarsPadding].
 *
 * @param modifier Standard [Modifier] applied to the [Scaffold].
 * @param contentAlignment Alignment strategy for the center [Box] container.
 * @param loading Boolean trigger to switch between [AppLogo] (loader) and [content].
 * @param loadingDelayMillis The grace period in milliseconds to wait before showing the loader.
 * @param loadingMinDisplayTimeMillis The minimum time in milliseconds the loader stays visible once shown.
 * @param loadingText Textual information shown to the user during loading.
 * @param loadingBackground Whether the loader should include its themed background.
 * @param loadingProgress Progress (0.0 to 1.0) used for deterministic loading animations.
 * @param onBackClick Callback for the TopBar back button.
 * @param onHelpClick Callback for the TopBar help button.
 * @param onMusicClick Callback for the TopBar music toggle.
 * @param onFeedbackClick Callback for the TopBar feedback button.
 * @param title Composable slot for the TopBar's central title area.
 * @param topBar Supplemental content slot appended to the top section.
 * @param bottomBar Supplemental content slot appended to the bottom section.
 * @param snackbarHost Dedicated slot for a Snackbar.
 * @param content The main screen content, displayed only when loading is complete.
 */
@Composable
internal fun Screen(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingMinDisplayTimeMillis: Long = 800L,
    loadingText: String = "",
    loadingBackground: Boolean = false,
    loadingProgress: Float? = null,
    onBackClick: (() -> Unit)? = null,
    onHelpClick: (() -> Unit)? = null,
    onMusicClick: (() -> Unit)? = null,
    onFeedbackClick: (() -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    topBar: @Composable ColumnScope.() -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                TopBar(
                    title = title,
                    onBackClick = onBackClick,
                    onHelpClick = onHelpClick,
                    onMusicClick = onMusicClick,
                    onFeedbackClick = onFeedbackClick
                )
                topBar()
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                bottomBar()
            }
        },
        snackbarHost = snackbarHost
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            // Prevent UI flickering with a grace period and a minimum display time
            val showLoading = showLoading(
                loading = loading,
                loadingDelayMillis = loadingDelayMillis,
                loadingMinDisplayTimeMillis = loadingMinDisplayTimeMillis
            )
            when (showLoading) {
                true -> AppLogo(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    showBackground = loadingBackground,
                    showProgress = true,
                    progress = loadingProgress,
                    text = loadingText,
                )

                false -> content()
            }
        }
    }
}

@Preview
@Composable
private fun ScreenLoadingPreview() = Preview {
    Screen(
        loading = true,
        loadingDelayMillis = 0L,
        loadingText = "Loading...",
        loadingProgress = 0.5f,
        onBackClick = {},
        onHelpClick = {},
        onMusicClick = {},
        onFeedbackClick = {},
    )
}

@Preview
@Composable
private fun ScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "new_version",
                value = "New Version!"
            ),
        )
    )
    Screen(
        loading = false,
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "Banner",
            )
        },
        onBackClick = {},
        onHelpClick = {},
        onMusicClick = {},
        onFeedbackClick = {},
        content = { Text(text = "Text") }
    )
}
