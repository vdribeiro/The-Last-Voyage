package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.gameSessionFinished
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.score.ScoreState

@Preview
@Composable
private fun ScoreNull() {
    val navigation = navigation(
        screen = Screen.SCORE,
        stateBuilder = ScoreState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun ScoreLoading() {
    val navigation = navigation(
        screen = Screen.SCORE,
        stateBuilder = ScoreState(
            loading = true,
            gameSessions = listOf(gameSessionFinished)
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun ScoreList() {
    val navigation = navigation(
        screen = Screen.SCORE,
        stateBuilder = ScoreState(
            loading = false,
            gameSessions = listOf(gameSessionFinished)
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
