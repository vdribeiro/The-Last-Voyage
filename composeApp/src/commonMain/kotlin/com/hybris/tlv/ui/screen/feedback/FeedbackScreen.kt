package com.hybris.tlv.ui.screen.feedback

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.component.container.Feedback
import com.hybris.tlv.theme.modifier.clearFocus
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun FeedbackScreen(store: Store<FeedbackState, FeedbackAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        store = store,
        modifier = Modifier.clearFocus(),
        help = false,
        feedback = false
    ) {
        Feedback(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            isError = storeState.isError,
            showThanks = storeState.showThanks,
            feedback = storeState.feedback,
            sendFeedback = { store.send(action = FeedbackAction.SendFeedback(message = it)) }
        )
    }
}

@Preview
@Composable
private fun FeedbackScreenPreview() = AppTheme {
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
private fun FeedbackScreenThanksPreview() = AppTheme {
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
private fun FeedbackScreenErrorPreview() = AppTheme {
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
        store = Store(
            initialState = FeedbackState(
                isError = true,
                feedback = "",
                showThanks = false
            )
        )
    )
}
