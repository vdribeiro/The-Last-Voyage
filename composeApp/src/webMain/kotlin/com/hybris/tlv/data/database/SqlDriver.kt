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
            const base = window.location.href.split('?')[0].split('#')[0].replace('index.html', '');
            const workerUrl = new URL('sqljs.worker.js', base).href;
            const wasmUrl = new URL('sql-wasm.wasm', base).href;
            
            console.log("SQLDelight: Resolving Worker -> " + workerUrl);
            console.log("SQLDelight: Resolving WASM -> " + wasmUrl);

            const worker = new Worker(workerUrl, { type: 'module' });

            worker.onerror = function(e) {
                console.error("Worker Execution Error:", e);
            };

            worker.postMessage({
                action: 'init',
                wasmLocation: wasmUrl
            });

            return worker;
        })()
    """
)
