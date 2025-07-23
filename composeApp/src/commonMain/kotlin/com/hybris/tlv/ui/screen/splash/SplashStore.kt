package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.Core
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.delay

internal sealed interface SplashAction {
    data object Start: SplashAction
}

internal data class SplashState(
    val progress: Float = 0f,
)

internal class SplashStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: SplashState,
    private val core: Core,
): Store<SplashAction, SplashState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launch {
        core.setup()

        // Uncomment to rewrite all data
        //core.rewrite().last()

        //core.sync().collect { result ->
        //    val progress = when (result) {
        //        is SyncResult.Error, SyncResult.Success -> 1f
        //        is SyncResult.Loading -> if (result.total > 0f) result.progress / result.total else 1f
        //    }
        //    updateState { it.copy(progress = progress) }
        //}

        core.prepopulate()

        updateState { it.copy(progress = 1f) }
        delay(timeMillis = 1000)
        send(action = SplashAction.Start)
    }

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Start -> navigate(screen = Navigation.Screen.MAIN_MENU)
        }
    }
}
