package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.flow.Dispatcher

internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    inMemory: Boolean
): SqlDriver = withContext(context = Dispatcher.IO) {
    val schema = schema.synchronous()
    AndroidSqliteDriver(
        schema = schema,
        context = applicationContext,
        name = name,
        callback = object: AndroidSqliteDriver.Callback(schema = schema) {
            override fun onConfigure(db: SupportSQLiteDatabase) {
                super.onConfigure(db = db)
                db.enableWriteAheadLogging()
            }
        }
    )
}
