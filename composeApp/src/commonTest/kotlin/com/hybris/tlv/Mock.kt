package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.StoreFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

private val testDispatchers by lazy { TestDispatchers() }

internal val mockCore: Core by lazy {
    Core(
        dispatcher = testDispatchers,
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines.mockEngine,
    )
}

internal val storeFactory: StoreFactory by lazy {
    StoreFactory(
        dispatcher = mockCore.dispatcher,
        navigation = mockCore.navigation,
        audioPlayer = mockCore.audioPlayer,
        config = mockCore.config,
        useCases = mockCore.useCases
    )
}

private val mockNavigation = object: NavigationManager {
    override val stateFlow: StateFlow<NavigationManager.State> get() = throw IllegalStateException("StateFlow has no value")
    override var back: () -> Unit = {}
}

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = testDispatchers,
    navigation = mockNavigation,
    audioPlayer = null,
    initialState = initialState
)
