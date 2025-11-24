package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
internal fun Switch(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    MaterialSwitch(
        modifier = modifier,
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange
    )
}

@Preview
@Composable
private fun SwitchPreview() = AppTheme {
    Column {
        Switch(checked = true)
        Switch(checked = false)
        Switch(checked = true, enabled = false)
    }
}
