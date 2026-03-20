package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.platform
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.resource.ImageResource
import com.hybris.tlv.ui.Preview
import thelastvoyage.composeapp.generated.resources.Res

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    image: ImageResource? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    if (!LocalInspectionMode.current && (platform == Platform.Android || platform == Platform.Ios)) ImageWithPath(
        modifier = modifier,
        path = image?.path,
        contentDescription = contentDescription,
        contentScale = contentScale
    ) else ImageWithResource(
        modifier = modifier,
        drawable = image?.drawable,
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
        path?.let { Res.getUri(path = it) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to get path", throwable = it)
    }.getOrNull()
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
    if (drawable == null) Box(modifier = modifier) else {
        Image(
            modifier = modifier,
            painter = painterResource(resource = drawable),
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    }
}

@Preview
@Composable
private fun ImagePreview() = Preview {
    Image(image = ImageResource.LauncherForeground)
}

private const val TAG = "Image"
