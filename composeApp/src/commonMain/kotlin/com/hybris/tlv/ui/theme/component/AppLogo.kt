package com.hybris.tlv.ui.theme.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_background
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun AppLogo(
    modifier: Modifier = Modifier,
    showBackground: Boolean = false,
    showProgress: Boolean = false,
    progress: Float? = null,
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
            if (showProgress) {
                when {
                    progress != null -> {
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress,
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.size(size = 160.dp),
                            progress = { animatedProgress },
                        )
                    }

                    else -> CircularProgressIndicator(modifier = Modifier.size(size = 160.dp))
                }
            }
        }

        Text(
            text = text,
            style = typography.headlineLarge,
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
