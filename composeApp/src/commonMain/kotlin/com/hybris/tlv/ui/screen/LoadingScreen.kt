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
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.container.Feedback

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
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
            Translation(
                key = "error_screen__thanks",
                value = "You are awesome too!"
            ),
            Translation(
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
