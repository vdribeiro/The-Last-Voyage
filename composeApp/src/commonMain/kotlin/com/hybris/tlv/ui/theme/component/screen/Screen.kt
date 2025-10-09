package com.hybris.tlv.ui.theme.component.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.container.Scaffold
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.image.Icon
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A scaffold-based screen that handles displaying a loading indicator or the primary content.
 * This composable is designed to prevent UI flickers during data loading by employing a two-part strategy:
 * - An initial grace period [loadingDelayMillis] to avoid showing the loader for very fast operations.
 * - A minimum display time [loadingMinDisplayTimeMillis] to ensure that if the loader does appear, it remains on screen long enough to avoid the same problem.
 */
@Composable
internal fun Screen(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    loadingDelayMillis: Long = 300L,
    loadingMinDisplayTimeMillis: Long = 800L,
    loadingText: String = "",
    loadingProgress: Float? = null,
    onMusicClick: (() -> Unit)? = null,
    onFeedbackClick: (() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Sound button
                    onMusicClick?.let {
                        Button(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Music"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(weight = 1f))
                    // Feedback button
                    onFeedbackClick?.let {
                        Button(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "Feedback"
                            )
                        }
                    }
                }
                topBar()
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val inspection = LocalInspectionMode.current
            var show by remember { mutableStateOf(value = inspection) }
            var loaderShownMark by remember { mutableStateOf<TimeMark?>(value = null) }
            LaunchedEffect(key1 = loading) {
                when {
                    loading -> {
                        delay(timeMillis = loadingDelayMillis)
                        loaderShownMark = TimeSource.Monotonic.markNow()
                        show = true
                    }

                    else -> when (val shownMark = loaderShownMark) {
                        null -> show = false
                        else -> {
                            val remainingTime = loadingMinDisplayTimeMillis - shownMark.elapsedNow().inWholeMilliseconds
                            if (remainingTime > 0) delay(timeMillis = remainingTime)
                            show = false
                            loaderShownMark = null
                        }
                    }
                }
            }
            when {
                show -> AppLogo(
                    showBackground = true,
                    showProgress = true,
                    progress = loadingProgress,
                    text = loadingText
                )

                else -> content()
            }
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() = AppTheme {
    Screen(
        loading = true,
        loadingDelayMillis = 0L,
        loadingProgress = 0.5f,
        loadingText = "Loading...",
    )
}
