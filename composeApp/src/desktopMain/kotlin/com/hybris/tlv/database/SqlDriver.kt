package com.hybris.tlv.database

import java.io.File
import java.util.Properties
import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.storage.appDataDir
import com.hybris.tlv.storage.deleteFile
import com.hybris.tlv.telemetry.Telemetry

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = JdbcSqliteDriver(
    url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else {
        "jdbc:sqlite:${File(appDataDir, name).absolutePath}"
    },
    properties = Properties(),
    schema = schema,
).apply {
    execute(
        identifier = null,
        sql = "PRAGMA journal_mode=WAL;",
        parameters = 0,
        binders = null
    )
}

internal actual suspend fun deleteDatabase(name: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        deleteFile(path = name)
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to delete database: $name", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Database"
