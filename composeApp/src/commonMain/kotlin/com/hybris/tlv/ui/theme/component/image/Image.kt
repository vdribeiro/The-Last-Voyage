package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage
import com.hybris.tlv.image.LauncherForeground
import com.hybris.tlv.platform.Platform
import com.hybris.tlv.platform.getPlatform
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
    val platform = getPlatform()
    if (LocalInspectionMode.current || platform == Platform.Windows || platform == Platform.Linux) {
        ImageWithResource(
            modifier = modifier,
            drawable = image?.drawable,
            contentDescription = contentDescription,
            contentScale = contentScale
        )
        return
    }

    ImageWithPath(
        modifier = modifier,
        path = image?.path,
        contentDescription = contentDescription,
        contentScale = contentScale
    )
}

@Composable
private fun ImageWithPath(
    modifier: Modifier = Modifier,
    path: String? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val model = runCatching {
        path?.let { Res.getUri(path = "drawable/$it") }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get path", throwable = it) }.getOrNull()
    if (model == null) Box(modifier = modifier) else AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        onError = { Telemetry.error(tag = TAG, message = "Unable to draw image", throwable = it.result.throwable) },
    )
}

@Composable
private fun ImageWithResource(
    modifier: Modifier = Modifier,
    drawable: DrawableResource? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val painter = runCatching {
        drawable?.let { painterResource(resource = it) }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get painter", throwable = it) }.getOrNull()
    if (painter == null) Box(modifier = modifier) else {
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    }
}

internal data class ImageResource(
    val path: String? = null,
    val drawable: DrawableResource? = null
)

@Preview
@Composable
private fun ImagePreview() = AppTheme {
    Image(image = LauncherForeground)
}

private const val TAG = "Image"
