package com.hybris.tlv.ui.theme.component.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.Preview

@Composable
internal fun LoadingScreen() {
    Screen(
        contentAlignment = Alignment.Center,
        loading = true,
        loadingDelayMillis = 0L,
        loadingBackground = true
    )
}

@Preview
@Composable
private fun LearnMenuPreview() = Preview {
    LoadingScreen()
}
