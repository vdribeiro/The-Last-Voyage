package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.text.Text
import androidx.compose.material3.Scaffold as MaterialScaffold

@Composable
internal fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    MaterialScaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        content = content
    )
}

@Preview
@Composable
private fun ScaffoldPreview() = AppTheme {
    Scaffold(
        topBar = { Text(text = "Top Bar") },
        bottomBar = { Text(text = "Bottom Bar") },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Content")
            }
        }
    )
}
