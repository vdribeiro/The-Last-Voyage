package com.hybris.tlv.ui.theme.component.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(contentColor = Color.White),
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        content = content
    )
}

@Preview
@Composable
private fun ButtonPreview() = AppTheme {
    Button()
}