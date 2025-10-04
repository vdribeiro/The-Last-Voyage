package com.hybris.tlv.ui.screen.newgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.screen.newgame.content.NewGameContent
import com.hybris.tlv.ui.screen.newgame.content.StartContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        modifier = Modifier.testTag(tag = NEW_GAME_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> NewGameContent(store = store)
            Content.START -> StartContent(store = store)
        }
    }
}

@Preview
@Composable
private fun NewGameLoading() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = true,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = null,
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameShip() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = ShipState(
                        totalPoints = 10,
                        sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 3),
                        materials = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                        fuel = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                        cryopods = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                    ),
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameStart() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.START,
                    selectedCatastrophe = Catastrophe(
                        id = "Asteroid Impact",
                        description = "A massive asteroid collides with Earth. The impact wipes out most life on the planet.",
                    ),
                    shipState = null,
                )
            )
        )
    }
}
