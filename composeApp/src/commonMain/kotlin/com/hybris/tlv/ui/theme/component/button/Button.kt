package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun Button(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hapticFeedback: HapticFeedbackType? = null,
    text: String = "",
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    onClick: () -> Unit = {},
) {
    val haptics = LocalHapticFeedback.current
    val typography = LocalTypography.current

    OutlinedButton(
        modifier = modifier,
        onClick = {
            hapticFeedback?.let { haptics.performHapticFeedback(hapticFeedbackType = it) }
            onClick()
        },
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
    hapticFeedback: HapticFeedbackType? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val haptics = LocalHapticFeedback.current
    IconButton(
        modifier = modifier,
        onClick = {
            hapticFeedback?.let { haptics.performHapticFeedback(hapticFeedbackType = it) }
            onClick()
        },
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
