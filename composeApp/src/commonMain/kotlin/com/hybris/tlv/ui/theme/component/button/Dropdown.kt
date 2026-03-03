package com.hybris.tlv.ui.theme.component.button

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.container.Scaffold
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun <T> Dropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onDismissRequest: () -> Unit = {},
    items: ImmutableList<T> = persistentListOf(),
    enabled: (T) -> Boolean = { true },
    text: (T) -> String? = { null },
    onClick: (T) -> Unit = {},
    leadingIcon: (T) -> (@Composable () -> Unit)? = { null }
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
                leadingIcon = leadingIcon(item) ?: { Icon(emptySize = 8.dp) }
            )
        }
    }
}

@Preview
@Composable
private fun DropdownPreview() = Preview {
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier.padding(paddingValues = innerPadding),
                contentAlignment = Alignment.TopStart
            ) {
                Dropdown(
                    expanded = true,
                    items = persistentListOf(
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
    )
}