package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.ui.store.StoreFactory

internal val mock: Core by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngine
    )
}

internal val errorMock: Core by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngineError
    )
}

internal val storeFactory: StoreFactory by lazy {
    StoreFactory(
        dispatcher = mock.dispatcher,
        navigation = mock.navigation,
        config = mock.config,
        useCases = mock.useCases
    )
}
