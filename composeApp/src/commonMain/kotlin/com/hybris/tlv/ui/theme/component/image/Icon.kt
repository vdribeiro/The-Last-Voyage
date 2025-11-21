package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun Icon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = defaultIcon,
    tint: Color = LocalContentColor.current,
    contentDescription: String = "Icon",
) {
    MaterialIcon(
        modifier = modifier,
        imageVector = imageVector,
        tint = tint,
        contentDescription = contentDescription
    )
}

internal val defaultIcon = Icons.Default.Apps

@Preview
@Composable
private fun IconPreview() = AppTheme {
    Icon()
}
