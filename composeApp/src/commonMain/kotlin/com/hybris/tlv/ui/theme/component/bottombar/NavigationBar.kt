package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.image.defaultIcon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun NavigationBar(
    modifier: Modifier = Modifier,
    items: List<NavigationItem> = emptyList()
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                label = { Text(text = item.label, textAlign = TextAlign.Center, maxLines = 1) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                selected = item.selected,
                onClick = item.onClick,
            )
        }
    }
}

internal data class NavigationItem(
    val label: String = "",
    val icon: ImageVector = defaultIcon,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)

@Preview
@Composable
private fun NavigationBarPreview() = AppTheme {
    NavigationBar()
}
