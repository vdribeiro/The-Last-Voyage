package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.kofi

@Composable
internal fun MainBottomBar(
    modifier: Modifier = Modifier,
    onCreditsClick: () -> Unit = {},
    developerCornerUri: String = "",
    supportUri: String = "",
) {
    val uriHandler = LocalUriHandler.current
    val websiteTranslation = remember { getTranslation(key = "website") }
    val creditsTranslation = remember { getTranslation(key = "main_menu_screen__credits") }

    val typography = LocalTypography.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable { uriHandler.openUri(uri = developerCornerUri) },
            text = websiteTranslation,
            style = typography.titleSmall,
        )
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable(onClick = onCreditsClick),
            text = creditsTranslation,
            style = typography.titleSmall,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable { uriHandler.openUri(uri = supportUri) },
            painter = painterResource(resource = Res.drawable.kofi),
            contentDescription = "Support",
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview
@Composable
private fun MainBottomBarPreview() = AppTheme {
    MainBottomBar()
}
