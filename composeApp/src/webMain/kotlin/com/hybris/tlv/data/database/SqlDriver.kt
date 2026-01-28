package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    inMemory: Boolean
): SqlDriver = withContext(context = Dispatcher.IO) {
    val worker: Worker = if (flags.devMode) getDebugWorker() else getWorker()
    WebWorkerDriver(worker = worker).also { driver ->
        schema.create(driver = driver).await()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun getDebugWorker(): Worker = js(
    code = """
        new Worker(
            new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)
        )
    """
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun getWorker(): Worker = js(
    code = """
        (function() {
            const path = window.location.origin + '/The-Last-Voyage/';
            const workerUrl = path + 'sqljs.worker.js';
            const wasmUrl = path + 'sql-wasm.wasm';

            const code = "self.locateFile = () => '" + wasmUrl + "'; import('" + workerUrl + "');";
            const blob = new Blob([code], { type: 'application/javascript' });
            const blobUrl = URL.createObjectURL(blob);
            
            console.log("SQLDelight: Worker URL -> " + workerUrl);
            console.log("SQLDelight: WASM URL -> " + wasmUrl);

            return new Worker(blobUrl, { type: 'module' });
        })()
    """
)
