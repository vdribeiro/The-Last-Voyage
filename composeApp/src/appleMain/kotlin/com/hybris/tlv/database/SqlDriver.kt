package com.hybris.tlv.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.JournalMode
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.storage.deleteFile
import com.hybris.tlv.telemetry.Telemetry

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = NativeSqliteDriver(
    schema = schema,
    name = name,
    onConfiguration = { config ->
        config.copy(
            inMemory = inMemory,
            journalMode = JournalMode.WAL
        )
    }
)

internal actual suspend fun deleteDatabase(name: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        deleteFile(path = name)
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to delete database: $name", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Database"
