package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.newgame.Content
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.ShipState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun NewGameLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = true,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = shipState,
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameShip() {
    TranslationCache.set(translations = translations)
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = shipState,
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameStart() {
    TranslationCache.set(translations = translations)
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.START,
                    selectedCatastrophe = catastrophes.random(),
                    shipState = shipState,
                )
            )
        )
    }
}

private val shipState: ShipState by lazy {
    ShipState(
        sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
        materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
    )
}

private val catastrophes: List<Catastrophe> by lazy {
    listOf(
        Catastrophe(
            id = "catastrophe__asteroid_impact",
            description = "catastrophe__asteroid_impact_description",
        ),
        Catastrophe(
            id = "catastrophe__nuclear_war",
            description = "catastrophe__nuclear_war_description",
        ),
    )
}
