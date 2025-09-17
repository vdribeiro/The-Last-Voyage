package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun AppLogo(
    size: Int = 160,
    showText: Boolean = true
) {
    val typography = LocalTypography.current

    Image(
        modifier = Modifier
            .size(size = size.dp)
            .clip(shape = CircleShape),
        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
        contentDescription = "Logo",
        contentScale = ContentScale.Crop,
    )
    if (showText) {
        val appTranslation = remember { getTranslation(key = "app_name") }
        Text(
            text = appTranslation,
            style = typography.headlineLarge,
        )
    }
}
