package com.hybris.tlv.ui.screen.feedback

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun FeedbackScreen(store: Store<FeedbackState, FeedbackAction>) {
    val storeState by store.stateFlow.collectAsState()
    val isError = storeState.isError
    val showThanks = storeState.showThanks
    var feedbackText by remember { mutableStateOf(value = storeState.feedback) }

    // Feedback translations depend on if it is was an app error or it's just simple user feedback
    val titleTranslation = remember { getTranslation(key = if (isError) "error_screen__title" else "error_screen__title_alt") }
    val descriptionTranslation = remember { getTranslation(key = if (isError) "error_screen__description" else "error_screen__description_alt") }
    val buttonTranslation = remember { getTranslation(key = "error_screen__button") }
    val thanksTranslation = remember { getTranslation(key = "error_screen__thanks") }

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier.testTag(tag = FEEDBACK_SCREEN),
        onMusicClick = { store.toggleAudio() },
    ) {
        Column(
            modifier = Modifier
                .testTag(tag = FEEDBACK_SCREEN_COLUMN)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Icon and title
            Icon(
                modifier = Modifier
                    .testTag(tag = FEEDBACK_SCREEN_ICON)
                    .size(size = 64.dp),
                imageVector = Icons.Outlined.BugReport,
                contentDescription = "Error Icon",
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                modifier = Modifier.testTag(tag = FEEDBACK_SCREEN_TITLE),
                text = titleTranslation,
                style = typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
                modifier = Modifier.testTag(tag = FEEDBACK_SCREEN_DESCRIPTION),
                text = descriptionTranslation,
                style = typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(height = 24.dp))

            // Feedback input
            OutlinedTextField(
                modifier = Modifier
                    .testTag(tag = FEEDBACK_SCREEN_INPUT)
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
                modifier = Modifier.testTag(tag = FEEDBACK_SCREEN_BUTTON),
                onClick = { store.send(action = FeedbackAction.SendFeedback(message = feedbackText)) },
                colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                enabled = feedbackText.isNotBlank() && !showThanks
            ) {
                Text(text = buttonTranslation)
            }

            if (showThanks) {
                // Thank you message
                Spacer(modifier = Modifier.height(height = 16.dp))
                Text(
                    modifier = Modifier.testTag(tag = FEEDBACK_SCREEN_THANKS),
                    text = thanksTranslation,
                    style = typography.headlineSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun Feedback() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = false,
                    feedback = "This game is awesome!",
                    showThanks = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedbackThanks() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
            Translation(
                key = "error_screen__thanks",
                value = "You are awesome too!"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = false,
                    feedback = "This game is awesome!",
                    showThanks = true
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedbackError() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title",
                value = "Oops! Something went wrong."
            ),
            Translation(
                key = "error_screen__description",
                value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, but a description from you would be invaluable."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = true,
                    feedback = "",
                    showThanks = false
                )
            )
        )
    }
}
