package com.hybris.tlv.ui.theme.component.container

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Surface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Surface(modifier = modifier, content = content)
}

@Preview
@Composable
private fun SurfacePreview() = AppTheme {
    Surface()
}