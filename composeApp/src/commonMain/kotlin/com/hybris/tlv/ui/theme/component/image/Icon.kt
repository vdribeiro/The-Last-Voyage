package com.hybris.tlv.ui.theme.component.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalColorScheme
import androidx.compose.material3.Icon as MaterialIcon

@Composable
internal fun Icon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null,
    emptySize: Dp = 16.dp
) {
    if (imageVector != null) MaterialIcon(
        modifier = modifier,
        imageVector = imageVector,
        tint = tint,
        contentDescription = contentDescription
    ) else Box(modifier = Modifier.padding(all = emptySize))
}

@Preview
@Composable
private fun IconPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        val colorScheme = LocalColorScheme.current
        Icon(imageVector = Icons.Default.Apps, tint = colorScheme.primary)
        Icon()
        Icon(imageVector = Icons.Default.Apps)
    }
}
