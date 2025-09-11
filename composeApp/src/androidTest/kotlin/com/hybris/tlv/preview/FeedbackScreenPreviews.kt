package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState

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
