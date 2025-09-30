package com.hybris.tlv

import androidx.compose.runtime.Composable
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.NavigationState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.StoreFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val testDispatchers by lazy {
    TestDispatchers()
}

internal val testCore: Core by lazy {
    Core(
        dispatcher = testDispatchers,
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines.testEngine,
    )
}

internal val storeFactory: StoreFactory by lazy {
    StoreFactory(
        dispatcher = testCore.dispatcher,
        navigation = testCore.navigation,
        audioPlayer = testCore.audioPlayer,
        config = testCore.config,
        useCases = testCore.useCases
    )
}

private val testNavigation = object: NavigationManager {
    override val stateFlow: StateFlow<NavigationState> = MutableStateFlow(value = NavigationState())
    override var back: () -> Unit = {}
    override fun goBack() {}
    override fun navigate(screen: NavigationManager.Screen, stateBuilder: Any?, savableState: Any?) {}
    @Composable
    override fun Screen(navigationState: NavigationState) {
    }
}

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = testDispatchers,
    navigation = testNavigation,
    audioPlayer = null,
    initialState = initialState
)
