package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.navigation.backNavigation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.progress.showLoading
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.topbar.TopBar
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.model.Translation

/**
 * A scaffold-based screen that handles displaying a loading indicator or the primary content.
 * This composable is designed to prevent UI flickering during data loading by employing a two-part strategy:
 * - An initial grace period [loadingDelayMillis] to avoid showing the loader for very fast operations.
 * - A minimum display time [loadingMinDisplayTimeMillis] to ensure that if the loader does appear, it remains on screen long enough to avoid the same problem.
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
            .fillMaxSize()
            .backNavigation { onBackClick?.invoke() },
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
                    text = loadingText
                )

                false -> content()
            }
        }
    }
}

@Preview
@Composable
private fun ScreenLoadingPreview() = AppTheme {
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
private fun ScreenPreview() = AppTheme {
    TranslationCache.set(
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
