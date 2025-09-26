package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.credit.CreditUseCases
import kotlinx.coroutines.Job

internal class CreditStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager?,
    audioPlayer: AudioPlayer?,
    private val creditUseCases: CreditUseCases
): Store<CreditState, CreditAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = CreditState(
        loading = true,
        credits = emptyList()
    )
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        val credits = creditUseCases.getCredits()
        updateState {
            it.copy(
                loading = false,
                credits = credits
            )
        }
    }

    override fun back(state: CreditState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }
}
