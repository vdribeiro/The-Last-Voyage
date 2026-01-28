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

            const blob = new Blob([''], { type: 'application/javascript' });
            const worker = new Worker(URL.createObjectURL(blob), { type: 'module' });

            fetch(workerUrl)
                .then(r => r.text())
                .then(workerCode => {
                    const finalCode = "self.locateFile = () => '" + wasmUrl + "';\n" + workerCode;
                    const finalBlob = new Blob([finalCode], { type: 'application/javascript' });
                    
                })
                .catch(e => console.error("Worker Script Fetch Failed", e));

            return (function() {
                const w = new Worker(URL.createObjectURL(new Blob([
                    "onmessage = function(e) { " +
                    "  if (e.data.action === 'boot') { " +
                    "    self.locateFile = () => e.data.wasmUrl; " +
                    "    import(e.data.workerUrl); " +
                    "  } " +
                    "}"
                ], { type: 'application/javascript' })), { type: 'module' });

                w.postMessage({
                    action: 'boot',
                    workerUrl: workerUrl,
                    wasmUrl: wasmUrl
                });

                w.postMessage({
                    action: 'init',
                    wasmLocation: wasmUrl
                });
                
                return w;
            })();
        })()
    """
)
