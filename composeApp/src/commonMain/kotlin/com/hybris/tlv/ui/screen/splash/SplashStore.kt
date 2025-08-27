package com.hybris.tlv.ui.screen.splash

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

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
            syncUseCases.setup()
            combine(
                flows = listOf(
                    syncUseCases.prepopulate(),
                    syncUseCases.sync()
                )
            ) { it.combine() }.collectProgress { progress ->
                updateState { it.copy(progress = progress) }
            }

            updateState { it.copy(progress = 1f) }
            delay(timeMillis = 1000)
            send(action = SplashAction.Start)
        }
    }

    override fun setBackNavigation(state: SplashState): () -> Unit = {}

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Start -> navigate(screen = Screen.MAIN_MENU)
        }
    }

    private fun Array<SyncResult>.combine(): SyncResult {
        val errorList = filterIsInstance<SyncResult.Error>()
        if (errorList.isNotEmpty()) return SyncResult.Error(error = errorList.joinToString(separator = "\n") { it.error })
        if (all { it is SyncResult.Success }) return SyncResult.Success
        val progress = map { if (it is SyncResult.Loading) it.progress / it.total else 1f }.sum() / size
        return SyncResult.Loading(progress = progress, total = 1f)
    }

    private suspend fun Flow<SyncResult>.collectProgress(update: (Float) -> Unit) = collect { result ->
        val progress = when (result) {
            is SyncResult.Error, SyncResult.Success -> 1f
            is SyncResult.Loading -> if (result.total > 0f) result.progress / result.total else 1f
        }
        update(progress)
    }
}
