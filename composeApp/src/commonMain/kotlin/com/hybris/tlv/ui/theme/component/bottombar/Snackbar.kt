package com.hybris.tlv.ui.theme.component.bottombar

import kotlinx.coroutines.withTimeoutOrNull
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.component.container.Scaffold

@Composable
internal fun Snackbar(
    modifier: Modifier = Modifier,
    message: String? = null,
    buttonText: String? = null,
    durationMillis: Long = if (buttonText != null) Long.MAX_VALUE else 3000L,
    onDismiss: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = message) {
        withTimeoutOrNull(timeMillis = durationMillis) {
            snackbarHostState.showSnackbar(
                message = message.orEmpty(),
                actionLabel = buttonText,
                duration = SnackbarDuration.Indefinite
            )
        }
        onDismiss()
    }
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
    )
}

@Preview
@Composable
private fun SnackbarPreview() = Preview {
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
private fun SnackbarWithButtonPreview() = Preview {
    Scaffold(
        snackbarHost = {
            Snackbar(
                message = "Snackbar",
                buttonText = "Action"
            )
        }
    )
}
