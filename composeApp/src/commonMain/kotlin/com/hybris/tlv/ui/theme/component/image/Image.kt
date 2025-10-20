package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
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
    path: String? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        modifier = modifier,
        model = runCatching { path?.let { Res.getUri(path = "drawable/$it") } }.getOrNull(),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    model: Any? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

@Preview
@Composable
private fun ImagePreview() = AppTheme {
    Image(
        model = Image(
            painter = painterResource(Res.drawable.ic_launcher_foreground),
            contentDescription = null,
        )
    )
}
