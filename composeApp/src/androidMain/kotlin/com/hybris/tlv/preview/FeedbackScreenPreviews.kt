package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Preview
@Composable
private fun Feedback() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = false,
                    feedback = "This game is awesome!",
                    showThanks = false
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedbackThanks() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            Translation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
            Translation(
                key = "error_screen__thanks",
                value = "You are awesome too!"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = false,
                    feedback = "This game is awesome!",
                    showThanks = true
                )
            )
        )
    }
}

@Preview
@Composable
private fun FeedbackError() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "error_screen__title",
                value = "Oops! Something went wrong."
            ),
            Translation(
                key = "error_screen__description",
                value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, but a description from you would be invaluable."
            ),
            Translation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    AppTheme {
        FeedbackScreen(
            store = getStore(
                initialState = FeedbackState(
                    isError = true,
                    feedback = "",
                    showThanks = false
                )
            )
        )
    }
}
