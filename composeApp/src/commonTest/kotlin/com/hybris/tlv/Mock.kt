package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.StoreFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val testDispatchers by lazy { TestDispatchers() }

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

private val navigation = object: NavigationManager {
    override val stateFlow: StateFlow<NavigationManager.State> = MutableStateFlow(value = NavigationManager.State())
    override var back: () -> Unit = {}
}

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = testDispatchers,
    navigation = navigation,
    audioPlayer = null,
    initialState = initialState
)
