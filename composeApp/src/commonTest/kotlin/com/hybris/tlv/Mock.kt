package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.StoreFactory

internal val mockCore: Core by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngine,
        audioPlayer = null
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

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = TestDispatchers(),
    navigation = null,
    audioPlayer = null,
    initialState = initialState
)
