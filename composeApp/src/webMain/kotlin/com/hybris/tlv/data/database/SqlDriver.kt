package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.hybris.tlv.core.flow.Dispatcher
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    inMemory: Boolean
): SqlDriver = withContext(context = Dispatcher.IO) {
    WebWorkerDriver(worker = getWorker()).also { driver ->
        schema.create(driver = driver).await()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun getWorker(): Worker = js(
    code = """
        new Worker(
            new URL('sqljs.worker.js', document.baseURI).href, 
            { type: 'module' }
        )
    """
)
