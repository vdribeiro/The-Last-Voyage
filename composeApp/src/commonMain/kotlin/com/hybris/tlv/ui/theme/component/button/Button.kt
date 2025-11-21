package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "",
    onClick: () -> Unit = {},
) {
    val typography = LocalTypography.current

    OutlinedButton(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        content = {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = typography.labelLarge
            )
        }
    )
}

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        content = content
    )
}

@Preview
@Composable
private fun ButtonPreview() = AppTheme {
    Button(text = "Button")
}

@Preview
@Composable
private fun IconButtonPreview() = AppTheme {
    Button(onClick = {}) { Icon() }
}
