package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.usecase.translation.model.domain.Translation

@Preview
@Composable
private fun FeedbackNull() {
    val navigation = navigation(
        screen = Screen.FEEDBACK,
        state = FeedbackState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun FeedbackWithTagAndMessage() {
    val navigation = navigation(
        screen = Screen.FEEDBACK,
        state = FeedbackState(
            tag = "Sum Ting Wong",
            message = "Bang Ding Ow"
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

private val translations = listOf(
    Translation(
        key = "error_screen__title",
        value = "Oops! Something went wrong."
    ),
    Translation(
        key = "error_screen__title_alt",
        value = "Feedback"
    ),
    Translation(
        key = "error_screen__description",
        value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, but a description from you would be invaluable."
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
        value = "Thank you for your feedback!"
    ),
)
