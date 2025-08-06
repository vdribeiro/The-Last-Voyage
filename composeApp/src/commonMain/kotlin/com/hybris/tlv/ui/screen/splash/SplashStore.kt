package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.Core
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.collectProgress
import com.hybris.tlv.usecase.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine

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
        combine(
            flows = listOf(
                core.setup(),
                core.prepopulate()
            )
        ) { it.combine() }.collectProgress { progress ->
            updateState { it.copy(progress = progress) }
        }

        launchAndForget {
            // Uncomment to rewrite all data
            //core.rewrite().last()
            // TODO
            //core.sync().last()
        }

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
