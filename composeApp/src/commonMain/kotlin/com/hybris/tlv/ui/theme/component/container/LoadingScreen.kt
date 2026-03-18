package com.hybris.tlv.ui.theme.component.container

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.core.telemetry.Console
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen

@Composable
internal fun LoadingScreen() {
    val isPreview = LocalInspectionMode.current
    var showFeedbackButton: Boolean by remember { mutableStateOf(value = isPreview) }
    var showFeedback: Boolean by remember { mutableStateOf(value = isPreview) }
    var showThanks: Boolean by remember { mutableStateOf(value = isPreview) }
    var feedback: String by remember { mutableStateOf(value = "") }
    var logs: String? by remember { mutableStateOf(value = null) }

    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 5000)
        showFeedbackButton = true
        while (isActive) {
            delay(timeMillis = 500)
            logs = Console.getSnapshot().joinToString(separator = "\n").ifBlank { null }
        }
    }
    Screen(
        contentAlignment = if (showFeedback) Alignment.TopStart else Alignment.Center,
        loading = !showFeedback,
        loadingDelayMillis = 0L,
        loadingBackground = true,
        onBackClick = null,
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
                showThanks = showThanks,
                feedback = feedback,
                sendFeedback = {
                    Telemetry.feedback(message = it)
                    showThanks = true
                },
                logs = logs
            )
        }
    )
}

@Preview
@Composable
private fun LearnMenuPreview() = Preview {
    LoadingScreen()
}
