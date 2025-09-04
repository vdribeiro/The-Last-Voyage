package com.hybris.tlv.ui.screen.gameover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.Score
import com.hybris.tlv.ui.component.TypewriterText
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameOverScreen(store: Store<GameOverAction, GameOverState>) {
    val storeState by store.stateFlow.collectAsState()
    val gameSession = storeState.gameSession
    val ship = gameSession?.ship
    val gameOverTranslation = remember { getTranslation(key = "game_over_screen__game_over") }
    val messageTranslation = remember { getTranslation(key = "game_over_screen__score") }
    val scoreTranslation = remember { getTranslation(key = "game_over_screen__end") }

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
                        text = gameOverTranslation,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    when (storeState.currentContent) {
                        Content.MESSAGE -> TypewriterText(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxWidth(),
                            text = getTranslation(key = storeState.gameOverMessage.orEmpty())
                        )

                        Content.SCORE -> if (gameSession != null && ship != null) Score(
                            isExpanded = null,
                            score = (gameSession.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                            utc = gameSession.utc,
                            yearsTraveled = ship.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                            sensorRange = ship.sensorRange.toString(),
                            integrity = ship.integrity.toString(),
                            materials = ship.materials.toString(),
                            fuel = ship.fuel.toString(),
                            cryopods = ship.cryopods.toString()
                        )
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
                        text = when (storeState.currentContent) {
                            Content.MESSAGE -> messageTranslation
                            Content.SCORE -> scoreTranslation
                        }
                    )
                }
            }
        }
    }
}
