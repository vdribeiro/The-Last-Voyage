package com.hybris.tlv.data.database

import java.util.Properties
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.applicationContext

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = if (!inMemory) AndroidSqliteDriver(
    schema = schema,
    context = applicationContext,
    name = name,
    callback = object: AndroidSqliteDriver.Callback(schema = schema) {
        override fun onConfigure(db: SupportSQLiteDatabase) {
            super.onConfigure(db = db)
            db.enableWriteAheadLogging()
        }
    }
) else JdbcSqliteDriver(
    url = JdbcSqliteDriver.IN_MEMORY,
    properties = Properties(),
    schema = schema,
)
