package com.hybris.tlv.ui.navigation

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry

internal open class NavigationManager(initialState: NavigationState) {
    private val mutex = Mutex()

    /**
     * The current state of the navigation.
     */
    private val stack: MutableList<NavigationState> = mutableListOf(NavigationState())
    private val _stateFlow: MutableStateFlow<NavigationState> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<NavigationState> = _stateFlow.asStateFlow()

    /**
     * Callback for the back action used by the [BackHandler].
     */
    var back: () -> Unit = { goBack() }

    /**
     * Goes back to the previous screen.
     */
    fun goBack(): Job = Dispatcher.Main.launch {
        mutex.withLock {
            stack.removeLastOrNull()
            stack.lastOrNull()?.let { navigationState -> _stateFlow.update { navigationState } }
            Telemetry.info(tag = TAG, message = "Go back to ${_stateFlow.value}")
        }
    }

    /**
     * Updates the [stateBuilder] of the current screen.
     */
    fun saveState(stateBuilder: Any?): Job = Dispatcher.Main.launch {
        mutex.withLock {
            stack.removeLastOrNull()?.let { navigationState -> stack.add(element = navigationState.copy(stateBuilder = stateBuilder)) }
        }
    }

    /**
     * Navigates to the new [navigationState].
     */
    fun navigate(navigationState: NavigationState): Job = Dispatcher.Main.launch {
        mutex.withLock {
            clean(navigationState = navigationState)
            stack.add(element = navigationState)
            _stateFlow.update { navigationState }
            Telemetry.info(tag = TAG, message = "Navigate to ${_stateFlow.value}")
        }
    }

    private fun clean(navigationState: NavigationState) {
        // Reset back callback
        back = { goBack() }
        // If the screen is already in the stack, truncate from that element onwards
        val index = stack.indexOfFirst { navigationState.screen == it.screen }
        if (index != -1) stack.subList(fromIndex = index, toIndex = stack.size).clear()
    }


    companion object {
        private const val TAG = "NavigationManager"
    }
}
