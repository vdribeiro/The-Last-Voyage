package com.hybris.tlv.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.LocalTypography
import com.hybris.tlv.theme.component.text.Text

/**
 * A toggle with [text] on the left and a switch on the right.
 */
@Composable
internal fun Toggle(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String? = null,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    val typography = LocalTypography.current

    Row(
        modifier = modifier.clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = typography.bodyLarge)
        Switch(
            enabled = enabled,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
private fun TogglePreview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Toggle(
            text = "Toggle",
            checked = true,
        )
        Toggle(
            text = "Toggle",
            checked = false,
        )
        Toggle(
            enabled = false,
            text = "Toggle",
            checked = true,
        )
        Toggle(checked = true)
    }
}