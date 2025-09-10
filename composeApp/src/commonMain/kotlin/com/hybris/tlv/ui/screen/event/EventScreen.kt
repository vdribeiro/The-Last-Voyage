package com.hybris.tlv.ui.screen.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.component.TypewriterText
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun EventScreen(store: Store<EventAction, EventState>) {
    val storeState by store.stateFlow.collectAsState()
    val event = storeState.event
    val children = storeState.children
    val ship = storeState.gameSession?.ship

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StatusBar(
                modifier = Modifier.statusBarsPadding(),
                hull = ship?.integrity?.toString() ?: "0",
                fuel = ship?.fuel?.toString() ?: "0",
                materials = ship?.materials?.toString() ?: "0",
                cryopods = ship?.cryopods?.toString() ?: "0"
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (event != null) {
                    // Event
                    Text(
                        text = getTranslation(key = event.id),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    TypewriterText(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxWidth(),
                        text = getTranslation(key = event.description) + if (event.outcome != null) "\n\n${event.outcome.getTranslation()}" else ""
                    )

                    // Event chain buttons
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                    ) {
                        children.orEmpty().ifEmpty { listOf(null) }.forEach { child ->
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                                onClick = { store.send(action = EventAction.Select(event = child)) }
                            ) {
                                Text(text = getTranslation(key = child?.id ?: "event__default_continue"))
                            }
                        }
                        Spacer(modifier = Modifier.height(height = 16.dp))
                    }
                }
            }
        }
    }
}
