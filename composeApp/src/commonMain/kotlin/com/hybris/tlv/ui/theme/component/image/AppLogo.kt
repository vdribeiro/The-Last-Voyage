package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.progress.ProgressIndicator
import com.hybris.tlv.ui.theme.component.text.Text
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_background
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun AppLogo(
    modifier: Modifier = Modifier,
    showBackground: Boolean = false,
    progress: Float? = null,
    showProgress: Boolean = progress != null,
    text: String = "",
) {
    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
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
                modifier = Modifier
                    .size(size = 200.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
            )
            if (showProgress) ProgressIndicator(modifier = Modifier.size(size = 160.dp), progress = progress)
        }

        Text(
            text = text,
            style = typography.displaySmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun AppLogoPreview() = AppTheme {
    AppLogo()
}

@Preview
@Composable
private fun AppLogoBackGroundPreview() = AppTheme {
    AppLogo()
    AppLogo(
        showBackground = true,
        showProgress = true,
        progress = 0.5f,
        text = "Loading"
    )
}
