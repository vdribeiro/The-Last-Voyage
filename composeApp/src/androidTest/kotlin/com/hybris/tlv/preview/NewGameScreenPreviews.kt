package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.usecase.translation.model.domain.Translation

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

private val translations = listOf(
    Translation(
        key = "key",
        value = "value"
    ),
)