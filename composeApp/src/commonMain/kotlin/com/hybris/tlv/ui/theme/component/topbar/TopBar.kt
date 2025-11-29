package com.hybris.tlv.ui.theme.component.topbar

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
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
        if (title != null) title()
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
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        TopBar(
            title = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Banner",
                )
            },
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
