package com.hybris.tlv.ui.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val testDispatchers by lazy {
    Dispatchers()
}
private val testNavigation by lazy {
    object: NavigationManager {
        override val stateFlow: StateFlow<NavigationState> = MutableStateFlow(value = NavigationState())
        override var back: () -> Unit = {}
        override fun goBack() {}
        override fun navigate(screen: Screen, stateBuilder: Any?, savableState: Any?) {}
        @Composable
        override fun Screen(navigationState: NavigationState) {
        }
    }
}

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = testDispatchers,
    navigation = testNavigation,
    audioPlayer = null,
    initialState = initialState
)
