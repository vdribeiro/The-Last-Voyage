package com.hybris.tlv.ui.screen

import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.container.Feedback

/**
 * The initial landing screen displayed during the application's bootstrap phase.
 * This screen manages three distinct states to ensure a smooth user experience even during complex initialization:
 * 1. **Loading State:** Displays a progress indicator while platform engines and dependencies are being wired in the background.
 * 2. **Feedback Gating:** After a 5-second delay, a feedback button appears. This allows users to report issues if the app fails to transition to the Main Menu.
 * 3. **Diagnostic Mode:** If the user toggles the feedback view, the screen reveals the [logs] collected during startup, facilitating easier debugging of platform-specific initialization errors.
 *
 * @param modifier Standard Compose [Modifier].
 * @param loading Controls the visibility of the primary loading indicator.
 * @param logs A string representation of the startup log snapshot.
 * @param sendFeedback Lambda triggered when the user submits a feedback report.
 */
@Composable
internal fun LoadingScreen(
    modifier: Modifier = Modifier,
    loading: Boolean = true,
    logs: String? = null,
    sendFeedback: (String) -> Unit = {},
) {
    var showFeedbackButton: Boolean by remember { mutableStateOf(value = false) }
    var showFeedback: Boolean by remember { mutableStateOf(value = !loading) }
    var showThanks: Boolean by remember { mutableStateOf(value = false) }
    var feedback: String by remember { mutableStateOf(value = "") }

    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 5000)
        showFeedbackButton = true
    }
    Screen(
        modifier = modifier,
        contentAlignment = if (showFeedback) Alignment.TopStart else Alignment.Center,
        loading = !showFeedback,
        loadingDelayMillis = 0L,
        loadingBackground = true,
        onBackClick = if (showFeedback) {
            {
                showFeedback = false
            }
        } else null,
        onHelpClick = null,
        onMusicClick = null,
        onFeedbackClick = if (showFeedbackButton) {
            {
                showFeedback = !showFeedback
                showThanks = false
                feedback = ""
            }
        } else null,
        content = {
            Feedback(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                showThanks = showThanks,
                feedback = feedback,
                sendFeedback = {
                    sendFeedback(it)
                    showThanks = true
                },
                logs = logs
            )
        }
    )
}

@Preview
@Composable
private fun LoadingScreenPreview() = Preview {
    LoadingScreen()
}

@Preview
@Composable
private fun LoadingScreenFeedbackPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            PreviewTranslation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            PreviewTranslation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
            PreviewTranslation(
                key = "error_screen__thanks",
                value = "You are awesome too!"
            ),
            PreviewTranslation(
                key = "error_screen__console",
                value = "Stacktrace"
            ),
        )
    )
    LoadingScreen(
        loading = false,
        logs = "This is a log"
    )
}
