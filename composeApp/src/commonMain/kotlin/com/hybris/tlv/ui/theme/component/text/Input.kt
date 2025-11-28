package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.image.Icon

@Composable
internal fun Input(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = modifier
            .defaultMinSize(minHeight = 60.dp),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        textStyle = style,
        leadingIcon = leadingIcon
    )
}

@Preview
@Composable
private fun InputPreview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Input(
            enabled = true,
            value = "Input",
            leadingIcon = { Icon(imageVector = Icons.Default.Search) }
        )
        Input(
            enabled = true,
            value = "Input",
            maxLines = 10
        )
        Input(
            enabled = false,
            value = "Input",
        )
        Input(
            enabled = true,
            value = "A very very long text in this compose preview",
            maxLines = 1
        )
        Input()
    }
}