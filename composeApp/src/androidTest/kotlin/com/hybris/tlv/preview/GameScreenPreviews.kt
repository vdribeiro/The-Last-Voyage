package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.ship
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.game.Content
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun GameLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameScreen(
            store = getStore(
                initialState = GameState(
                    loading = true,
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
            store = getStore(
                initialState = GameState(
                    loading = false,
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
            store = getStore(
                initialState = GameState(
                    loading = false,
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
            store = getStore(
                initialState = GameState(
                    loading = false,
                    currentContent = Content.TRAVEL,
                    ship = ship,
                    currentStellarHost = null,
                    nearStellarHosts = stellarHosts.shuffled().take(n = 3),
                )
            )
        )
    }
}
