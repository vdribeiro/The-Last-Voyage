package com.hybris.tlv.ui.theme.component.topbar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.open
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    banner: String? = null,
    onBackClick: (() -> Unit)? = null,
    onHelpClick: (() -> Unit)? = null,
    onMusicClick: (() -> Unit)? = null,
    onFeedbackClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        onBackClick?.let {
            Button(onClick = it) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        banner?.let {
            val uriHandler = LocalUriHandler.current
            val typography = LocalTypography.current
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { uriHandler.open(uri = banner) },
                text = getTranslation(key = "new_version"),
                style = typography.labelLarge,
            )
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
        onHelpClick?.let {
            Button(onClick = it) {
                Icon(
                    imageVector = Icons.Default.QuestionMark,
                    contentDescription = "Help"
                )
            }
        }
        onMusicClick?.let {
            Button(onClick = it) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music"
                )
            }
        }
        onFeedbackClick?.let {
            Button(onClick = it) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Feedback"
                )
            }
        }
    }
}

@Preview
@Composable
private fun HostDefinitionPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "new_version",
                value = "New Version!"
            ),
        )
    )
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        TopBar(
            banner = "Banner",
            onBackClick = {},
            onHelpClick = {},
            onMusicClick = {},
            onFeedbackClick = {},
        )
        TopBar(
            onBackClick = {},
            onFeedbackClick = {},
        )
        TopBar(
            onHelpClick = {},
            onMusicClick = {},
        )
        TopBar()
    }
}
