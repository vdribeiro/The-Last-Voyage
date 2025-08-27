package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.credit.model.Credit

internal sealed interface CreditAction

internal data class CreditState(
    val credits: List<Credit> = emptyList(),
)

internal class CreditStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: CreditState?,
    private val creditUseCases: CreditUseCases
): Store<CreditAction, CreditState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState ?: CreditState()
) {
    init {
        if (initialState == null) setup()
    }

    private fun setup() = launchInPipeline {
        val credits = creditUseCases.getCredits()
        updateState { it.copy(credits = credits) }
    }

    override fun setBackNavigation(state: CreditState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: CreditState, action: CreditAction) {}
}
