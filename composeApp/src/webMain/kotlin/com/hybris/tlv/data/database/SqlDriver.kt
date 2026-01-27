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
        (function() {
            const path = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/') + 1);
            const workerUrl = path + 'sqljs.worker.js';
            const wasmUrl = path + 'sql-wasm.wasm';

            console.log("SQLDelight: Loading worker from: " + workerUrl);
            console.log("SQLDelight: Looking for WASM at: " + wasmUrl);

            const worker = new Worker(workerUrl, { type: "module" });

            worker.postMessage({
                action: 'init',
                wasmLocation: wasmUrl
            });

            return worker;
        })()
    """
)
