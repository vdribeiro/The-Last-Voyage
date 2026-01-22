package com.hybris.tlv.data.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import org.w3c.dom.Worker

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = WorkerDispatcherDriver(
    worker = Worker(scriptURL = "sqldelight-worker.js")
).also { driver ->
    schema.create(driver)
}
