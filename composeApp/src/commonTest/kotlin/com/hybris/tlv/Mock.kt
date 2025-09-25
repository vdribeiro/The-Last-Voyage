package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.MockNavigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.StoreFactory

internal val mock: Core by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngine,
        audioPlayer = AudioPlayer(),
    )
}

internal val storeFactory: StoreFactory by lazy {
    StoreFactory(
        dispatcher = mock.dispatcher,
        navigation = mock.navigation,
        audioPlayer = mock.audioPlayer,
        config = mock.config,
        useCases = mock.useCases
    )
}

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = TestDispatchers(),
    navigation = MockNavigation(),
    audioPlayer = AudioPlayer(),
    initialState = initialState
)
