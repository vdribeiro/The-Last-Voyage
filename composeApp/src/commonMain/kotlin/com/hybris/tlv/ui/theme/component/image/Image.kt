package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
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
    }.getOrNull() ?: runCatching {
        image?.drawable?.let { painterResource(resource = it) }
    }.getOrNull()
    AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

internal data class ImageResource(
    val path: String? = null,
    val drawable: DrawableResource? = null
)

@Preview
@Composable
private fun ImagePreview() = AppTheme {
    Image(image = ImageResource(drawable = Res.drawable.ic_launcher_foreground))
}
