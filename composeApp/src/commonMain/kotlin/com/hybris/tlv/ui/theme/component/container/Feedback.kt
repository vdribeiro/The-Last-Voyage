package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Input
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun Feedback(
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    showThanks: Boolean = false,
    feedback: String = "",
    sendFeedback: (String) -> Unit = {},
) {
    var feedbackText by remember { mutableStateOf(value = feedback) }

    val titleTranslation = getTranslation(key = if (isError) "error_screen__title" else "error_screen__title_alt")
    val descriptionTranslation = getTranslation(key = if (isError) "error_screen__description" else "error_screen__description_alt")
    val buttonTranslation = getTranslation(key = "error_screen__button")
    val thanksTranslation = getTranslation(key = "error_screen__thanks")

    val typography = LocalTypography.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = modifier.padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(size = 64.dp),
                imageVector = Icons.Outlined.BugReport,
                contentDescription = "Feedback Icon",
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp
                ),
                text = titleTranslation,
                style = typography.titleLarge
            )
            Text(
                text = descriptionTranslation,
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }

        Input(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 120.dp),
            enabled = !showThanks,
            value = feedbackText,
            onValueChange = { feedbackText = it },
        )
        Spacer(modifier = Modifier.height(height = 24.dp))

        Button(
            enabled = feedbackText.isNotBlank() && !showThanks,
            text = buttonTranslation,
            onClick = { sendFeedback(feedbackText) },
        )

        if (showThanks) {
            // Thank you message
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                text = thanksTranslation,
                style = typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
private fun FeedbackPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Description"
            ),
            Translation(
                key = "error_screen__button",
                value = "Send"
            ),
            Translation(
                key = "error_screen__thanks",
                value = "Thanks"
            ),
        )
    )
    Feedback(
        isError = false,
        showThanks = true,
        feedback = "This is awesome!",
    )
}

@Preview
@Composable
private fun FeedbackErrorPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title",
                value = "Error"
            ),
            Translation(
                key = "error_screen__description",
                value = "Description"
            ),
            Translation(
                key = "error_screen__button",
                value = "Send"
            ),
            Translation(
                key = "error_screen__thanks",
                value = "Thanks"
            ),
        )
    )
    Feedback(
        isError = true,
        showThanks = false,
        feedback = "This is awesome!",
    )
}