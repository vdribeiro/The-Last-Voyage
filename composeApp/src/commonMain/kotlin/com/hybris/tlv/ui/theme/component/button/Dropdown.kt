package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun Dropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    items: List<DropdownItem> = emptyList()
) {
    val typography = LocalTypography.current

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                enabled = item.enabled,
                text = {
                    Text(
                        text = item.text,
                        maxLines = 1,
                        style = typography.labelLarge
                    )
                },
                onClick = item.onClick,
                leadingIcon = item.leadingIcon
            )
        }
    }
}

internal data class DropdownItem(
    val enabled: Boolean = true,
    val text: String = "",
    val onClick: () -> Unit = {},
    val leadingIcon: @Composable (() -> Unit)? = null,
)

@Preview
@Composable
private fun DropdownPreview() = AppTheme {
    Dropdown(
        expanded = true,
        items = listOf(
            DropdownItem(text = "Item 1"),
            DropdownItem(text = "Item 2", leadingIcon = { Icon() }),
        )
    )
}