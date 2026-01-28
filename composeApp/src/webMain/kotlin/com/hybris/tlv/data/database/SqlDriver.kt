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
            const subfolder = '/The-Last-Voyage/';
            
            const workerUrl = subfolder + 'sqljs.worker.js';
            const wasmUrl = subfolder + 'sql-wasm.wasm';

            const wrapper = "self.locateFile = () => '" + wasmUrl + "'; importScripts('" + workerUrl + "');";
            
            const blob = new Blob([wrapper], { type: 'application/javascript' });
            const blobUrl = URL.createObjectURL(blob);
            
            console.log("SQLDelight: Loading worker from " + workerUrl);
            console.log("SQLDelight: Directing worker to WASM at " + wasmUrl);

            return new Worker(blobUrl);
        })()
    """
)