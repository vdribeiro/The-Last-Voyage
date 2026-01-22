package com.hybris.tlv.data.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = WebWorkerDriver(worker = getWorker()).also { driver ->
    schema.create(driver)
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun getWorker(): Worker = js(code = """new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")