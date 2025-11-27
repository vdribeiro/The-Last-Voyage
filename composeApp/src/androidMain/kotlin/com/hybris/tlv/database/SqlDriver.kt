package com.hybris.tlv.database

import kotlinx.coroutines.withContext
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.applicationContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = AndroidSqliteDriver(
    schema = schema,
    context = applicationContext,
    name = if (inMemory) null else name,
    callback = object: AndroidSqliteDriver.Callback(schema = schema) {
        override fun onConfigure(db: SupportSQLiteDatabase) {
            super.onConfigure(db = db)
            db.enableWriteAheadLogging()
        }
    }
)

internal actual suspend fun deleteDatabase(name: String): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        applicationContext.deleteDatabase(name)
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to delete database: $name", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Database"
