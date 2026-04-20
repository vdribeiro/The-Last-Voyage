package com.hybris.tlv.ui.screen.feedback

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.container.Feedback

@Composable
internal fun FeedbackScreen(store: Store<FeedbackState, FeedbackAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        onBackClick = { store.send(action = FeedbackAction.Back) },
        onHelpClick = null,
        onFeedbackClick = null
    ) {
        Feedback(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            isError = storeState.isError,
            showThanks = storeState.showThanks,
            feedback = storeState.feedback,
            sendFeedback = { store.send(action = FeedbackAction.SendFeedback(message = it)) },
            logs = storeState.logs
        )
    }
}

@Preview
@Composable
private fun FeedbackScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            PreviewTranslation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            PreviewTranslation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    FeedbackScreen(
        store = Store(
            initialState = FeedbackState(
                isError = false,
                feedback = "This game is awesome!",
                showThanks = false
            )
        )
    )
}

@Preview
@Composable
private fun FeedbackScreenThanksPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "error_screen__title_alt",
                value = "Feedback"
            ),
            PreviewTranslation(
                key = "error_screen__description_alt",
                value = "Your insights are valuable, whether you have an idea or have found something that isn't working right."
            ),
            PreviewTranslation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
            PreviewTranslation(
                key = "error_screen__thanks",
                value = "You are awesome too!"
            ),
        )
    )
    FeedbackScreen(
        store = Store(
            initialState = FeedbackState(
                isError = false,
                feedback = "This game is awesome!",
                showThanks = true
            )
        )
    )
}

@Preview
@Composable
private fun FeedbackScreenErrorPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "error_screen__title",
                value = "Oops! Something went wrong."
            ),
            PreviewTranslation(
                key = "error_screen__description",
                value = "It seems we've run into an uncharted anomaly. Our engineering crew is already on it, but a description from you would be invaluable."
            ),
            PreviewTranslation(
                key = "error_screen__button",
                value = "Submit Feedback"
            ),
        )
    )
    FeedbackScreen(
        store = Store(
            initialState = FeedbackState(
                isError = true,
                feedback = "",
                showThanks = false
            )
        )
    )
}
