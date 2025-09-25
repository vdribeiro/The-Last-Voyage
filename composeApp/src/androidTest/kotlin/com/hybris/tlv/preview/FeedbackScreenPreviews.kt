package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun Feedback() {
    TranslationCache.set(translations = translations)
    AppTheme {
        FeedbackScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
                initialState = FeedbackState(
                    isError = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedbackError() {
    TranslationCache.set(translations = translations)
    AppTheme {
        FeedbackScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = MockNavigation(),
                audioPlayer = AudioPlayer(),
                initialState = FeedbackState(
                    isError = true
                )
            )
        )
    }
}
