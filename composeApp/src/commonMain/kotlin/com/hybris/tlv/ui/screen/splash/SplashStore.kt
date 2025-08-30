package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.sync.collectProgress
import kotlinx.coroutines.delay

internal sealed interface SplashAction {
    data object Start: SplashAction
}

internal data class SplashState(
    val progress: Float = 0f,
)

internal class SplashStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: SplashState,
    private val syncUseCases: SyncUseCases,
): Store<SplashAction, SplashState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() {
        launchAndForget {
            // Uncomment to get archive
            //syncUseCases.getArchive().last()
        }
        launch {
            syncUseCases.sync().collectProgress { progress ->
                updateState { it.copy(progress = progress) }
            }

            updateState { it.copy(progress = 1f) }
            delay(timeMillis = 1000)
            send(action = SplashAction.Start)
        }
    }

    override fun setBackNavigation(): () -> Unit = {}

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Start -> navigate(screen = Screen.MAIN_MENU)
        }
    }
}
