package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage
import com.hybris.tlv.image.LauncherForeground
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.AppTheme
import thelastvoyage.composeapp.generated.resources.Res

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    image: ImageResource? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    if (LocalInspectionMode.current) {
        val painter = getPainter(image = image)
        if (painter == null) Box(modifier = modifier) else {
            Image(
                modifier = modifier,
                painter = painter,
                contentDescription = contentDescription,
                contentScale = contentScale
            )
        }
        return
    }

    val model = getUri(image)
    if (model == null) Box(modifier = modifier) else AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        onError = { Telemetry.error(tag = TAG, message = "Unable to draw image", throwable = it.result.throwable) },
    )
}

internal data class ImageResource(
    val path: String? = null,
    val drawable: DrawableResource? = null
)

private fun getUri(image: ImageResource?): String? = runCatching {
    image?.path?.let { Res.getUri(path = "drawable/$it") }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get path", throwable = it) }.getOrNull()

@Composable
private fun getPainter(image: ImageResource?): Painter? = runCatching {
    image?.drawable?.let { painterResource(resource = it) }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get painter", throwable = it) }.getOrNull()

@Preview
@Composable
private fun ImagePreview() = AppTheme {
    Image(image = LauncherForeground)
}

private const val TAG = "Image"
