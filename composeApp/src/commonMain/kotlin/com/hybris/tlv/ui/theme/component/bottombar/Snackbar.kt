package com.hybris.tlv.ui.theme.component.bottombar

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Snackbar(
    modifier: Modifier = Modifier,
    messages: List<String> = emptyList(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    rememberCoroutineScope().launch { messages.forEach { snackbarHostState.showSnackbar(message = it) } }
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
    )
}

@Preview
@Composable
private fun SnackbarPreview() = AppTheme {
    Snackbar(messages = listOf(element = "Snackbar"))
}
