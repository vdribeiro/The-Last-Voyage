package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Navigation manager with the screens index.
 */
internal open class NavigationManager(
    private val dispatcher: Dispatcher,
    initialState: NavigationState
) {

    /**
     * The current state of the navigation.
     */
    private val stack: MutableList<NavigationState> = mutableListOf(NavigationState())
    private val _stateFlow: MutableStateFlow<NavigationState> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<NavigationState> get() = _stateFlow

    /**
     * A callback for the back action, handled by the App's [BackHandler].
     */
    var back: () -> Unit = { goBack() }

    /**
     * Goes back to the previous screen.
     */
    fun goBack() {
        dispatcher.main.launch {
            if (stack.size > 1) {
                stack.removeLast()
                _stateFlow.update { stack.last() }
            }
            Telemetry.info(tag = TAG, message = "Go back to ${_stateFlow.value}")
        }
    }

    /**
     * Updates the state of the current navigation and then navigates to a new screen given a new state.
     */
    fun navigate(screen: Screen, stateBuilder: Any? = null, savableState: Any? = null) {
        dispatcher.main.launch {
            if (stack.isNotEmpty()) stack[stack.lastIndex] = stack.last().copy(stateBuilder = savableState)
            val navigationState = NavigationState(screen = screen, stateBuilder = stateBuilder)
            val index = stack.indexOf(element = navigationState)
            if (index != -1) stack.subList(index, stack.size).clear()
            stack.add(element = navigationState)
            _stateFlow.value = navigationState
            Telemetry.info(tag = TAG, message = "Navigate to ${_stateFlow.value}")
        }
    }

    /**
     * Fallback to [Screen.Splash] screen.
     */
    protected fun fallback() = navigate(screen = Screen.Splash)

    /**
     * The main composable responsible for rendering the current screen based on the navigation state.
     */
    @Composable
    open fun Screen(navigationState: NavigationState) {
    }

    companion object {
        private const val TAG = "NavigationManager"
    }
}
