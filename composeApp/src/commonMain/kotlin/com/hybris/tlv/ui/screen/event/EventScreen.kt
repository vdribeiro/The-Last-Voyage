package com.hybris.tlv.ui.screen.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.component.TypewriterText
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun EventScreen(store: Store<EventState, EventAction>) {
    val storeState by store.stateFlow.collectAsState()
    val event = storeState.parentEvent
    val children = storeState.childrenEvents
    val ship = storeState.ship

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier.testTag(tag = EVENT_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            StatusBar(
                modifier = Modifier
                    .testTag(tag = EVENT_SCREEN_STATUS_BAR)
                    .statusBarsPadding(),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
    ) {
        Column(
            modifier = Modifier
                .testTag(tag = EVENT_SCREEN_COLUMN)
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Event
            Text(
                modifier = Modifier.testTag(tag = EVENT_SCREEN_COLUMN_EVENT),
                text = getTranslation(key = event.id),
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            TypewriterText(
                modifier = Modifier
                    .testTag(tag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION)
                    .weight(weight = 1f)
                    .fillMaxWidth(),
                text = getTranslation(key = event.description) + if (event.outcome != null) "\n\n${event.outcome.getTranslation()}" else ""
            )

            // Event chain buttons
            LazyColumn(
                modifier = Modifier
                    .testTag(tag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            ) {
                items(items = children, key = { it.id }) { child ->
                    Button(
                        modifier = Modifier
                            .testTag(tag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                        onClick = { store.send(action = EventAction.Select(event = child)) }
                    ) {
                        Text(text = getTranslation(key = child.id))
                    }
                }
                item { Spacer(modifier = Modifier.height(height = 16.dp)) }
            }
        }
    }
}
