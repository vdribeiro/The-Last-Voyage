package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.ship
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.game.Content
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.Tutorial
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun GameLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameState(
                    loading = true,
                    tutorialStep = Tutorial.NO,
                    currentContent = Content.SYSTEM,
                    ship = null,
                    currentStellarHost = null,
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameShip() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameState(
                    loading = false,
                    tutorialStep = Tutorial.NO,
                    currentContent = Content.SHIP,
                    ship = ship,
                    currentStellarHost = null,
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameSystem() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameState(
                    loading = false,
                    tutorialStep = Tutorial.NO,
                    currentContent = Content.SYSTEM,
                    ship = ship,
                    currentStellarHost = hostsWithPlanets.random(),
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameTravel() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameState(
                    loading = false,
                    tutorialStep = Tutorial.NO,
                    currentContent = Content.TRAVEL,
                    ship = ship,
                    currentStellarHost = null,
                    nearStellarHosts = stellarHosts.shuffled().take(n = 3),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameTutorial() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameState(
                    loading = false,
                    tutorialStep = Tutorial.YES,
                    currentContent = Content.SYSTEM,
                    ship = null,
                    currentStellarHost = null,
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}
