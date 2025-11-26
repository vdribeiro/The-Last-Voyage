package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
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
    onDismiss: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = message) {
        snackbarHostState.showSnackbar(
            message = message.orEmpty(),
            actionLabel = buttonText,
        )
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
