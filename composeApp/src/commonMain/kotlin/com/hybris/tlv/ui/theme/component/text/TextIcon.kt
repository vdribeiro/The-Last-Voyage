package com.hybris.tlv.ui.theme.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon

@Composable
internal fun TextIcon(
    modifier: Modifier = Modifier,
    text: String? = null,
    imageVector: ImageVector? = null,
) {
    val typography = LocalTypography.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = text,
            textAlign = TextAlign.End,
            style = typography.bodyLarge
        )
        Icon(imageVector = imageVector)
    }
}