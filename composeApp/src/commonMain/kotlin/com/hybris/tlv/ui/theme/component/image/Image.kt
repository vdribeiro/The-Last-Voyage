package com.hybris.tlv.ui.theme.component.image

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.hybris.tlv.ui.theme.AppTheme
import thelastvoyage.composeapp.generated.resources.Res

@Composable
internal fun Image(
    modifier: Modifier = Modifier,
    path: String? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        modifier = modifier,
        model = runCatching { path?.let { Res.getUri(path = "drawable/$it") } }.getOrNull(),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}

@Preview
@Composable
private fun ImagePreview() = AppTheme {
    Image(path = "ic_launcher_foreground.png")
}
