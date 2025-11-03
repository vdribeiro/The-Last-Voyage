package com.hybris.tlv.ui.screen.feedback

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Feedback
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun FeedbackScreen(store: Store<FeedbackState, FeedbackAction>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        onBackClick = { store.back() },
        onMusicClick = { store.toggleAudio() },
    ) {
        Feedback(
            isError = storeState.isError,
            showThanks = storeState.showThanks,
            feedback = storeState.feedback,
            sendFeedback = { store.send(action = FeedbackAction.SendFeedback(message = it)) }
        )
    }
}

@Preview
@Composable
private fun FeedbackPreview() = AppTheme {
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

@Preview
@Composable
private fun FeedbackThanksPreview() = AppTheme {
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

@Preview
@Composable
private fun FeedbackErrorPreview() = AppTheme {
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
