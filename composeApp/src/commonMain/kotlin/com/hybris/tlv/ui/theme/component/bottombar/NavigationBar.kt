package com.hybris.tlv.ui.theme.component.bottombar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal inline fun <T> NavigationBar(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    crossinline enabled: (T) -> Boolean = { true },
    crossinline selected: (T) -> Boolean = { false },
    crossinline text: (T) -> String? = { null },
    crossinline icon: (T) -> ImageVector? = { null },
    crossinline onClick: (T) -> Unit = {}
) {
    val typography = LocalTypography.current

    NavigationBar(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            val text = text(item)
            val icon = icon(item)
            NavigationBarItem(
                enabled = enabled(item),
                selected = selected(item),
                label = {
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = typography.labelLarge
                    )
                },
                icon = { Icon(imageVector = icon, contentDescription = text, emptySize = 12.dp) },
                onClick = { onClick(item) },
            )
        }
    }
}

@Preview
@Composable
private fun NavigationBarPreview() = Preview {
    NavigationBar(
        items = listOf(
            "Home",
            "Apps",
        ),
        text = { it },
        icon = { if (it == "Home") Icons.Default.Apps else null },
    )
}
