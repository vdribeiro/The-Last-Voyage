package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.gameSession
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.game.Content
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.Tutorial

@Preview
@Composable
private fun GameNull() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameLoading() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState(
            loading = true,
            tutorial = Tutorial.NO,
            currentContent = Content.SHIP,
            gameSession = gameSession,
            currentStellarHost = hostsWithPlanets.first(),
            nearStellarHosts = stellarHosts.shuffled().take(n = 3),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameShip() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState(
            loading = false,
            tutorial = Tutorial.NO,
            currentContent = Content.SHIP,
            gameSession = gameSession,
            currentStellarHost = hostsWithPlanets.first(),
            nearStellarHosts = stellarHosts.shuffled().take(n = 3),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameSystem() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState(
            loading = false,
            tutorial = Tutorial.NO,
            currentContent = Content.SYSTEM,
            gameSession = gameSession,
            currentStellarHost = hostsWithPlanets.first(),
            nearStellarHosts = stellarHosts.shuffled().take(n = 3),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameTravel() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState(
            loading = false,
            tutorial = Tutorial.NO,
            currentContent = Content.TRAVEL,
            gameSession = gameSession,
            currentStellarHost = hostsWithPlanets.first(),
            nearStellarHosts = stellarHosts.shuffled().take(n = 3),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameTutorial() {
    val navigation = navigation(
        screen = Screen.GAME,
        state = GameState(
            loading = false,
            tutorial = Tutorial.YES
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
