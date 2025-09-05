package com.hybris.tlv.ui.screen.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.colorScheme
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun ErrorScreen(store: Store<ErrorAction, ErrorState>) {
    val storeState by store.stateFlow.collectAsState()
    val isError = storeState.throwable != null
    var feedbackText by remember { mutableStateOf(value = "") }
    val titleTranslation = remember { getTranslation(key = if (isError) "error_screen__title" else "error_screen__title_alt") }
    val descriptionTranslation = remember { getTranslation(key = if (isError) "error_screen__description" else "error_screen__description_alt") }
    val buttonTranslation = remember { getTranslation(key = "error_screen__button") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(size = 64.dp),
                    imageVector = Icons.Outlined.BugReport,
                    contentDescription = "Error Icon",
                    tint = colorScheme.secondary
                )

                Spacer(Modifier.height(height = 16.dp))

                Text(
                    text = titleTranslation,
                    style = typography.headlineSmall
                )

                Spacer(Modifier.height(height = 8.dp))

                Text(
                    text = descriptionTranslation,
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(height = 24.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 120.dp),
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                )

                Spacer(Modifier.height(height = 24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { store.send(action = ErrorAction.SendFeedback(message = feedbackText)) },
                        enabled = feedbackText.isNotBlank()
                    ) {
                        Text(text = buttonTranslation)
                    }
                }
            }
        }
    }
}
