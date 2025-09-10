package com.hybris.tlv

import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines

internal val mock by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngine
    )
}

internal val errorMock by lazy {
    Core(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines().mockEngineError
    )
}
