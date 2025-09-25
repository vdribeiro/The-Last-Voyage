package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.gameSessionFinished
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun ScoreLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        ScoreScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
                initialState = ScoreState(
                    loading = true,
                    gameSessions = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun ScoreList() {
    TranslationCache.set(translations = translations)
    AppTheme {
        ScoreScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
                initialState = ScoreState(
                    loading = false,
                    gameSessions = listOf(gameSessionFinished)
                )
            )
        )
    }
}
