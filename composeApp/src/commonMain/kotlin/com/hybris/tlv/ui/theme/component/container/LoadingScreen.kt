package com.hybris.tlv.ui.theme.component.container

import kotlinx.coroutines.delay
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
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen

@Composable
internal fun LoadingScreen() {
    val isPreview = LocalInspectionMode.current
    var showFeedbackButton: Boolean by remember { mutableStateOf(value = isPreview) }
    var showFeedback: Boolean by remember { mutableStateOf(value = isPreview) }
    var logs: String? by remember { mutableStateOf(value = null) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 5000)
        showFeedbackButton = true
        logs = Console.getSnapshot().joinToString(separator = "\n")
    }
    Screen(
        contentAlignment = Alignment.Center,
        loading = true,
        loadingDelayMillis = 0L,
        loadingBackground = true,
        onBackClick = null,
        onHelpClick = null,
        onMusicClick = null,
        onFeedbackClick = if (showFeedbackButton) {
            { showFeedback = !showFeedback }
        } else null,
        content = {
            if (showFeedback) Console(logs = logs)
        }
    )
}

@Preview
@Composable
private fun LearnMenuPreview() = Preview {
    LoadingScreen()
}
