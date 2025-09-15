package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.catastrophes
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.formula
import com.hybris.tlv.shipState
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.screen.newgame.Content
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun NewGameLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        NewGameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                initialState = NewGameState(
                    loading = true,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = shipState,
                    formula = formula
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
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = shipState,
                    formula = formula
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameAdvanced() {
    TranslationCache.set(translations = translations)
    AppTheme {
        NewGameScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.ADVANCED,
                    selectedCatastrophe = null,
                    shipState = shipState,
                    formula = formula
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
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.START,
                    selectedCatastrophe = catastrophes.random(),
                    shipState = shipState,
                    formula = formula
                )
            )
        )
    }
}
