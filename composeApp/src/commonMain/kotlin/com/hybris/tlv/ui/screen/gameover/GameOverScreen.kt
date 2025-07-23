package com.hybris.tlv.ui.screen.gameover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.Score
import com.hybris.tlv.ui.component.TypewriterText
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun GameOverScreen(store: Store<GameOverAction, GameOverState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent ?: return
    val gameSession = storeState.gameSession ?: return

    BackHandler(enabled = true) { store.send(action = GameOverAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.weight(weight = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = getTranslation(key = "game_over_screen__game_over"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    when (currentContent) {
                        Content.MESSAGE -> TypewriterText(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxWidth(),
                            text = getTranslation(key = storeState.gameOverMessage.orEmpty())
                        )

                        Content.SCORE -> Column(
                            modifier = Modifier.verticalScroll(state = rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                        ) {
                            Score(
                                isExpanded = null,
                                score = (gameSession.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                                utc = gameSession.utc,
                                yearsTraveled = gameSession.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                                sensorRange = gameSession.sensorRange.toString(),
                                integrity = gameSession.integrity.toString(),
                                materials = gameSession.materials.toString(),
                                fuel = gameSession.fuel.toString(),
                                cryopods = gameSession.cryopods.toString()
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    onClick = { store.send(action = GameOverAction.Continue) }
                ) {
                    Text(
                        text = when (currentContent) {
                            Content.MESSAGE -> getTranslation(key = "game_over_screen__score")
                            Content.SCORE -> getTranslation(key = "game_over_screen__end")
                        }
                    )
                }
            }
        }
    }
}
