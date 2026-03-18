@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import org.w3c.dom.Worker

@ExcludeFromTesting
internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver = withContext(context = Dispatcher.IO) {
    WebWorkerDriver(worker = getWorker()).also { driver ->
        runCatching {
            schema.create(driver = driver).await()
        }.onFailure {
            Telemetry.error(tag = TAG, message = "Unable to create database schema.", throwable = it)
        }
    }
}

private fun getWorker(): Worker = js(
    code = """
        new Worker(
            new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)
        )
    """
)

private const val TAG = "SqlDriver"
