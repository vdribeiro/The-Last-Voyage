package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.FeedbackHeader
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Input
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun Feedback(
    isError: Boolean,
    showThanks: Boolean,
    feedback: String,
    sendFeedback: (String) -> Unit,
) {
    var feedbackText by remember { mutableStateOf(value = feedback) }

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val titleTranslation = remember(key1 = translationVersion) { getTranslation(key = if (isError) "error_screen__title" else "error_screen__title_alt") }
    val descriptionTranslation = remember(key1 = translationVersion) { getTranslation(key = if (isError) "error_screen__description" else "error_screen__description_alt") }
    val buttonTranslation = remember(key1 = translationVersion) { getTranslation(key = "error_screen__button") }
    val thanksTranslation = remember(key1 = translationVersion) { getTranslation(key = "error_screen__thanks") }

    val typography = LocalTypography.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FeedbackHeader(
            modifier = Modifier
                .padding(bottom = 24.dp),
            title = titleTranslation,
            description = descriptionTranslation
        )

        // Feedback input
        Input(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 120.dp),
            enabled = !showThanks,
            value = feedbackText,
            onValueChange = {
                feedbackText = it
            },
        )
        Spacer(modifier = Modifier.height(height = 24.dp))

        // Send feedback button
        Button(
            text = buttonTranslation,
            onClick = { sendFeedback(feedbackText) },
            enabled = feedbackText.isNotBlank() && !showThanks,
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
