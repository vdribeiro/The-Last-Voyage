package com.hybris.tlv.ui.screen.feedback

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
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
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun FeedbackScreen(store: Store<FeedbackAction, FeedbackState>) {
    val storeState by store.stateFlow.collectAsState()
    val isError = storeState.isError
    var feedbackText by remember { mutableStateOf(value = "") }
    var inputEnabled by remember { mutableStateOf(value = true) }
    var buttonEnabled by remember { mutableStateOf(value = false) }

    // Feedback translations depend on if it is was an app error or it's just simple user feedback
    val titleTranslation = remember { getTranslation(key = if (isError) "error_screen__title" else "error_screen__title_alt") }
    val descriptionTranslation = remember { getTranslation(key = if (isError) "error_screen__description" else "error_screen__description_alt") }
    val buttonTranslation = remember { getTranslation(key = "error_screen__button") }
    val thanksTranslation = remember { getTranslation(key = "error_screen__thanks") }

    Scaffold(
        modifier = Modifier
            .testTag(tag = FEEDBACK_SCREEN)
            .fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
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
                Spacer(Modifier.height(height = 16.dp))
                Text(
                    modifier = Modifier
                        .testTag(tag = FEEDBACK_SCREEN_TITLE),
                    text = titleTranslation,
                    style = typography.headlineSmall
                )
                Spacer(Modifier.height(height = 8.dp))
                Text(
                    modifier = Modifier
                        .testTag(tag = FEEDBACK_SCREEN_DESCRIPTION),
                    text = descriptionTranslation,
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(height = 24.dp))

                // Feedback input
                OutlinedTextField(
                    modifier = Modifier
                        .testTag(tag = FEEDBACK_SCREEN_INPUT)
                        .fillMaxWidth()
                        .height(height = 120.dp),
                    enabled = inputEnabled,
                    value = feedbackText,
                    onValueChange = {
                        feedbackText = it
                        buttonEnabled = feedbackText.isNotBlank()
                    },
                )
                Spacer(Modifier.height(height = 24.dp))

                // Send feedback button
                Button(
                    modifier = Modifier
                        .testTag(tag = FEEDBACK_SCREEN_BUTTON),
                    onClick = {
                        store.send(action = FeedbackAction.SendFeedback(message = feedbackText))
                        inputEnabled = false
                        buttonEnabled = false
                    },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    enabled = buttonEnabled
                ) {
                    Text(text = buttonTranslation)
                }
                if (!inputEnabled) {
                    Spacer(Modifier.height(height = 16.dp))
                    Text(
                        modifier = Modifier
                            .testTag(tag = FEEDBACK_SCREEN_THANKS),
                        text = thanksTranslation,
                        style = typography.headlineSmall
                    )
                }
            }
        }
    }
}
