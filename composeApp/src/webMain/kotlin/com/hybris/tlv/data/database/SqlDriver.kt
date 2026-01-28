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
            const isGitHub = window.location.hostname.includes('github.io');
            const subfolder = isGitHub ? '/The-Last-Voyage/' : '/';
            
            const workerUrl = new URL(subfolder + 'sqljs.worker.js', window.location.origin);

            const worker = new Worker(workerUrl, { type: 'module' });

            const wasmUrl = window.location.origin + subfolder + 'sql-wasm.wasm';
            worker.postMessage({
                action: 'init',
                wasmLocation: wasmUrl
            });

            console.log("SQLDelight: Worker created at " + workerUrl.href);
            return worker;
        })()
    """
)
