package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text

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
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text?.let { Text(text = it, style = typography.bodyLarge) }
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
    Toggle(
        text = "Toggle",
        checked = true,
        onCheckedChange = {}
    )
}