package com.hybris.tlv.ui.screen.credits

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.credits.CreditsUseCases
import com.hybris.tlv.usecase.credits.model.Credits

internal sealed interface CreditsAction {
    data object Back: CreditsAction
}

internal data class CreditsState(
    val credits: List<Credits> = emptyList(),
)

internal class CreditsStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: CreditsState,
    private val creditsUseCases: CreditsUseCases
): Store<CreditsAction, CreditsState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val credits = creditsUseCases.getCredits()
        updateState { it.copy(credits = credits) }
    }

    override fun reducer(state: CreditsState, action: CreditsAction) {
        when (action) {
            CreditsAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)
        }
    }
}
