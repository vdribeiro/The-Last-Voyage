package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.gameSessionFinished
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.gameover.Content
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.usecase.gamesession.model.GameOver

@Preview
@Composable
private fun GameOverNull() {
    val navigation = navigation(
        screen = Screen.GAME_OVER,
        state = GameOverState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameOverMessage() {
    val navigation = navigation(
        screen = Screen.GAME_OVER,
        state = GameOverState(
            currentContent = Content.MESSAGE,
            gameSession = gameSessionFinished,
            gameOver = GameOver.GAME_OVER
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun GameOverScore() {
    val navigation = navigation(
        screen = Screen.GAME_OVER,
        state = GameOverState(
            currentContent = Content.SCORE,
            gameSession = gameSessionFinished,
            gameOver = GameOver.GAME_OVER
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
