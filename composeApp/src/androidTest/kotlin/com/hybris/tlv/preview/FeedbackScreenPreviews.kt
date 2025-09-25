package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun Feedback() {
    TranslationCache.set(translations = translations)
    AppTheme {
        FeedbackScreen(
            store = getStore(
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
            store = getStore(
                initialState = FeedbackState(
                    isError = true
                )
            )
        )
    }
}
