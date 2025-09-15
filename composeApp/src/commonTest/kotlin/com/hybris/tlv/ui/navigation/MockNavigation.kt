package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class MockNavigation: NavigationManager {
    override val stateFlow: StateFlow<NavigationManager.State> = MutableStateFlow(value = NavigationManager.State())
    override var back: () -> Unit = {}
    override fun navigate(screen: NavigationManager.Screen, stateBuilder: Any?) {}
    @Composable
    override fun Screen(screen: NavigationManager.Screen, stateBuilder: Any?) {
    }
}
