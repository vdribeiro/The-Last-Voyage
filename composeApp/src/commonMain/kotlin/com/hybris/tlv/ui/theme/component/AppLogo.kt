package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalTypography
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_background
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun AppLogo(
    modifier: Modifier = Modifier,
    showBackground: Boolean = false,
    text: String? = null,
) {
    val typography = LocalTypography.current

    if (showBackground) {
        Image(
            modifier = Modifier
                .size(size = 160.dp)
                .clip(shape = CircleShape),
            painter = painterResource(resource = Res.drawable.ic_launcher_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
        )
    }
    Image(
        modifier = modifier
            .size(size = 200.dp)
            .clip(shape = CircleShape),
        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
        contentDescription = "Logo",
        contentScale = ContentScale.Crop,
    )
    if (text != null) {
        Text(
            text = text,
            style = typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
    }
}
