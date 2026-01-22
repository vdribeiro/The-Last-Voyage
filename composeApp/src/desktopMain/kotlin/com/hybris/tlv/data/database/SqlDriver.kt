package com.hybris.tlv.data.database

import java.io.File
import java.util.Properties
import kotlinx.coroutines.withContext
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.data.storage.appDataPath

internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    inMemory: Boolean
): SqlDriver = withContext(context = Dispatcher.IO) {
    JdbcSqliteDriver(
        url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else {
            "jdbc:sqlite:${File(appDataPath, name).absolutePath}"
        },
        properties = Properties(),
        schema = schema.synchronous(),
    ).apply {
        schema.create(driver = this).await()
        execute(
            identifier = null,
            sql = "PRAGMA journal_mode=WAL;",
            parameters = 0,
            binders = null
        ).await()
    }
}
