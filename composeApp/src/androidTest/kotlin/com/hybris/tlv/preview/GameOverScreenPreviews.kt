package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.gameSessionFinished
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.gameover.Content
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun GameOverLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameOverScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameOverState(
                    loading = true,
                    currentContent = Content.MESSAGE,
                    gameSession = null,
                    gameOver = null
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameOverMessage() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameOverScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameOverState(
                    loading = false,
                    currentContent = Content.MESSAGE,
                    gameSession = gameSessionFinished,
                    gameOver = GameOver.GAME_OVER
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameOverScore() {
    TranslationCache.set(translations = translations)
    AppTheme {
        GameOverScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = GameOverState(
                    loading = false,
                    currentContent = Content.SCORE,
                    gameSession = gameSessionFinished,
                    gameOver = GameOver.GAME_OVER
                )
            )
        )
    }
}
