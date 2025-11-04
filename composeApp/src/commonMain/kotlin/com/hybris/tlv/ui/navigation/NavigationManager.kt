package com.hybris.tlv.ui.navigation

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

internal open class NavigationManager(
    initialState: NavigationState
) {
    private val mutex = Mutex()

    /**
     * The current state of the navigation.
     */
    private val stack: MutableList<NavigationState> = mutableListOf(NavigationState())
    private val _stateFlow: MutableStateFlow<NavigationState> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<NavigationState> = _stateFlow.asStateFlow()

    /**
     * A callback for the back action, handled by the App's [BackHandler].
     */
    var back: () -> Unit = { goBack() }

    /**
     * Goes back to the previous screen.
     */
    fun goBack() {
        Dispatcher.Main.launch {
            mutex.withLock {
                stack.removeLastOrNull()
                stack.lastOrNull()?.let { navigationState -> _stateFlow.update { navigationState } }
                Telemetry.info(tag = TAG, message = "Go back to ${_stateFlow.value}")
            }
        }
    }

    /**
     * Updates the [currentState] of the screen and then navigates to the new [navigationState].
     */
    fun navigate(navigationState: NavigationState, currentState: Any? = null) {
        Dispatcher.Main.launch {
            mutex.withLock {
                // Edit last element of the stack
                stack.removeLastOrNull()?.let { navigationState -> stack.add(element = navigationState.copy(stateBuilder = currentState)) }
                // If the state is already in the stack, go from there
                val index = stack.indexOf(element = navigationState)
                if (index != -1) stack.subList(fromIndex = index + 1, toIndex = stack.size).clear()
                // Update state
                stack.add(element = navigationState)
                _stateFlow.update { navigationState }
                Telemetry.info(tag = TAG, message = "Navigate to ${_stateFlow.value}")
            }
        }
    }

    companion object {
        private const val TAG = "NavigationManager"
    }
}
