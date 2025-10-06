package com.hybris.tlv.ui.theme.component.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.text.Text
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(contentColor = Color.White),
        content = { Text(text = text) }
    )
}

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        content = content
    )
}

@Preview
@Composable
private fun ButtonPreview() = AppTheme {
    Button(text = "Button")
}
