package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.usecase.translation.model.domain.Translation

@Preview
@Composable
private fun EventNull() {
    val navigation = navigation(
        screen = Screen.EVENT,
        state = EventState()
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
