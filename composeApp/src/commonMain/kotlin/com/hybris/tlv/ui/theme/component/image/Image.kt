package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.AppTheme
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    image: ImageResource? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val model = runCatching {
        image?.path?.let { Res.getUri(path = "drawable/$it") }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get path", throwable = it) }.getOrNull() ?: runCatching {
        image?.drawable?.let { painterResource(resource = it) }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get painter", throwable = it) }.getOrNull()
    if (model != null) AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        onError = { Telemetry.error(tag = TAG, message = "Unable to draw image", throwable = it.result.throwable) }
    ) else Box(modifier = modifier)
}

internal data class ImageResource(
    val path: String? = null,
    val drawable: DrawableResource? = null
)

@Preview
@Composable
private fun ImagePathPreview() = AppTheme {
    Image(image = ImageResource(path = "terrestrial_planet.jpg"))
}

@Preview
@Composable
private fun ImageDrawablePreview() = AppTheme {
    Image(image = ImageResource(drawable = Res.drawable.ic_launcher_foreground))
}

private const val TAG = "Image"
