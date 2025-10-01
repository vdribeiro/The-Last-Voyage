package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.defaultEvent
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun EventLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        EventScreen(
            store = getStore(
                initialState = EventState(
                    loading = true,
                    ship = null,
                    parentEvent = defaultEvent,
                    childrenEvents = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun EventRandom() {
    TranslationCache.set(translations = translations)
    AppTheme {
        EventScreen(
            store = getStore(
                initialState = EventState(
                    loading = false,
                    ship = ship,
                    parentEvent = events.random(),
                    childrenEvents = events.shuffled().take(n = 3)
                )
            )
        )
    }
}
