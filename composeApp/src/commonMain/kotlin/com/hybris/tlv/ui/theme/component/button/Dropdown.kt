package com.hybris.tlv.ui.theme.component.button

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal inline fun <T> Dropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    noinline onDismissRequest: () -> Unit = {},
    items: List<T> = emptyList(),
    crossinline enabled: (T) -> Boolean = { true },
    crossinline text: (T) -> String? = { null },
    crossinline onClick: (T) -> Unit = {},
    crossinline leadingIcon: (T) -> @Composable (() -> Unit) = { { Icon(emptySize = 8.dp) } }
) {
    val typography = LocalTypography.current

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                enabled = enabled(item),
                text = {
                    Text(
                        text = text(item),
                        maxLines = 1,
                        style = typography.labelLarge
                    )
                },
                onClick = { onClick(item) },
                leadingIcon = leadingIcon(item)
            )
        }
    }
}

@Preview
@Composable
private fun DropdownPreview() = AppTheme {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Dropdown(
            expanded = true,
            items = listOf(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
            ),
            enabled = { it != "Item 2" },
            text = { it },
            leadingIcon = {
                if (it == "Item 1" || it == "Item 2") {
                    { Icon(imageVector = Icons.Default.Apps) }
                } else {
                    { Icon(emptySize = 8.dp) }
                }
            }
        )
    }
}