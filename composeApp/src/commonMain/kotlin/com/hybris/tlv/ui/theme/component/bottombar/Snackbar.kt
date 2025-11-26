package com.hybris.tlv.ui.theme.component.bottombar

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Scaffold

@Composable
internal fun Snackbar(
    modifier: Modifier = Modifier,
    message: String? = null,
    buttonText: String? = null,
    duration: Long = if (buttonText != null) Long.MAX_VALUE else 3000L,
    onDismiss: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = message) {
        snackbarHostState.currentSnackbarData?.dismiss()
        val timerJob = launch {
            delay(timeMillis = duration)
            snackbarHostState.currentSnackbarData?.dismiss()
        }
        snackbarHostState.showSnackbar(
            message = message.orEmpty(),
            actionLabel = buttonText,
            duration = SnackbarDuration.Indefinite
        )
        timerJob.cancel()
        onDismiss()
    }
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
    )
}

@Preview
@Composable
private fun SnackbarPreview() = AppTheme {
    Scaffold(
        snackbarHost = {
            Snackbar(
                message = "Snackbar"
            )
        }
    )
}

@Preview
@Composable
private fun SnackbarWithButtonPreview() = AppTheme {
    Scaffold(
        snackbarHost = {
            Snackbar(
                message = "Snackbar",
                buttonText = "Action"
            )
        }
    )
}
