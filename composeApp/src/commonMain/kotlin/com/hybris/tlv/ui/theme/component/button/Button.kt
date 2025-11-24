package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
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
    text: String? = null,
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
    Column {
        Button(text = "Button")
        Button(text = "Button", enabled = false)
        Button(text = null)
    }
}

@Preview
@Composable
private fun IconButtonPreview() = AppTheme {
    Column {
        Button(enabled = true, onClick = {}) { Icon(imageVector = Icons.Default.Apps) }
        Button(enabled = false, onClick = {}) { Icon(imageVector = Icons.Default.Apps) }
        Button(onClick = {}) { Icon() }
    }
}
