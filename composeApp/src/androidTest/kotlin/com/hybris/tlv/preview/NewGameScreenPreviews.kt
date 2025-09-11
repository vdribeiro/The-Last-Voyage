package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.catastrophes
import com.hybris.tlv.formula
import com.hybris.tlv.shipPrototype
import com.hybris.tlv.shipState
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.newgame.Content
import com.hybris.tlv.ui.screen.newgame.NewGameState

@Preview
@Composable
private fun NewGameNull() {
    val navigation = navigation(
        screen = Screen.NEW_GAME,
        state = NewGameState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun NewGameShip() {
    val navigation = navigation(
        screen = Screen.NEW_GAME,
        state = NewGameState(
            currentContent = Content.SHIP,
            selectedShip = shipPrototype,
            shipState = shipState,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun NewGameAdvanced() {
    val navigation = navigation(
        screen = Screen.NEW_GAME,
        state = NewGameState(
            currentContent = Content.ADVANCED,
            formula = formula
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun NewGameStart() {
    val navigation = navigation(
        screen = Screen.NEW_GAME,
        state = NewGameState(
            currentContent = Content.START,
            selectedCatastrophe = catastrophes.random(),
            selectedShip = shipPrototype,
            shipState = shipState,
            formula = formula
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
