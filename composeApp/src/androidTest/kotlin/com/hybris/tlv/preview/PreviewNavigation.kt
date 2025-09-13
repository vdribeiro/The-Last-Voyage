package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PreviewNavigation: NavigationManager {
    override val stateFlow: StateFlow<State> = MutableStateFlow(value = State())
    override var back: () -> Unit = {}
    override fun navigate(screen: NavigationManager.Screen, stateBuilder: Any?) {}
    @Composable
    override fun Screen(screen: NavigationManager.Screen, stateBuilder: Any?) {
    }
}
