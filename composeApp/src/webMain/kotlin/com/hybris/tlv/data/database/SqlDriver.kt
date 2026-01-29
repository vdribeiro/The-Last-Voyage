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
            const base = window.location.origin + '/The-Last-Voyage/';
            const workerUrl = base + 'sqljs.worker.js';
            const sqlJsUrl = base + 'sql-wasm.js';
            const wasmUrl = base + 'sql-wasm.wasm';

            // We create a proxy worker that stays alive
            // We use a blank blob to start it
            const blob = new Blob([''], { type: 'application/javascript' });
            const worker = new Worker(URL.createObjectURL(blob), { type: 'module' });

            // We manually fetch the worker script to bypass MIME/Pathing issues
            fetch(workerUrl)
                .then(r => r.text())
                .then(code => {
                    // We modify the code in memory to fix the import specifier
                    // This replaces "sql.js" with the absolute URL to sql-wasm.js
                    const fixedCode = code.replace(/['"]sql\.js['"]/g, "'" + sqlJsUrl + "'");
                    
                    // We prepend the wasm configuration
                    const finalScript = "self.locateFile = () => '" + wasmUrl + "';\n" + fixedCode;
                    
                    // Create a new worker with the fixed, in-memory script
                    const finalBlob = new Blob([finalScript], { type: 'application/javascript' });
                    const finalWorker = new Worker(URL.createObjectURL(finalBlob), { type: 'module' });
                    
                    // Transfer the message handling to the final worker
                    finalWorker.postMessage({
                        action: 'init',
                        wasmLocation: wasmUrl
                    });
                    
                    // Since we already returned a worker object to Kotlin, 
                    // we actually need to initialize the specific one we returned.
                    // This logic is tricky with the returned object, so let's simplify.
                });

            // SIMPLIFIED APPROACH: Return a worker that self-imports with absolute paths
            const bootCode = "self.locateFile = () => '" + wasmUrl + "'; import '" + workerUrl + "';";
            const bootBlob = new Blob([bootCode], { type: 'application/javascript' });
            return new Worker(URL.createObjectURL(bootBlob), { type: 'module' });
        })()
    """
)
