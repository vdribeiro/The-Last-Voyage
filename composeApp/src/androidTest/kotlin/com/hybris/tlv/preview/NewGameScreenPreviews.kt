package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.ui.screen.newgame.Content
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.ShipState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe

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
                        sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
                        materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
                        fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
                        cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
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
