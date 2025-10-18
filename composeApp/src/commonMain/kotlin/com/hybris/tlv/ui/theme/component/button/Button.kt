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
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    val typography = LocalTypography.current

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
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
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    IconButton(
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
