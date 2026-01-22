package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Input
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

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
        modifier = modifier
            .imePadding()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(size = 64.dp),
            imageVector = Icons.Outlined.BugReport,
            contentDescription = "Feedback Icon",
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            text = titleTranslation,
            style = typography.titleLarge
        )
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = descriptionTranslation,
            style = typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Input(
            modifier = Modifier
                .testTag(tag = "feedback_input")
                .fillMaxWidth()
                .height(height = 120.dp),
            enabled = !showThanks,
            value = feedbackText,
            onValueChange = { feedbackText = it },
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            enabled = !showThanks && (isError || feedbackText.isNotBlank()),
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
    InjectTranslations(
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
    InjectTranslations(
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