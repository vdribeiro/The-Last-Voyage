package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.events
import com.hybris.tlv.gameSession
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStateBuilder

@Preview
@Composable
private fun EventNull() {
    val navigation = navigation(
        screen = Screen.EVENT,
        stateBuilder = EventStateBuilder()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun EventRandom() {
    val navigation = navigation(
        screen = Screen.EVENT,
        stateBuilder = EventStateBuilder(
            gameSession = gameSession,
            events = events,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
