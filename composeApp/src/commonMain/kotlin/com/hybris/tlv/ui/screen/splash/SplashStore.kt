package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

internal class SplashStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    private val syncUseCases: SyncUseCases,
): Store<SplashAction, SplashState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = SplashState(
        progress = 0f
    )
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        // Uncomment to get archive
        //syncUseCases.getArchive().last()

        // Sync and return the progress
        syncUseCases.sync().collect { result ->
            val progress = when (result) {
                is SyncResult.Error, SyncResult.Success -> 1f
                is SyncResult.Loading -> if (result.total > 0f) result.progress / result.total else 1f
            }
            updateState { it.copy(progress = progress) }
        }

        updateState { it.copy(progress = 1f) }
        delay(timeMillis = 1000L)
        send(action = SplashAction.Start)
    }

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Start -> navigate(screen = Screen.MAIN_MENU)
        }
    }
}
