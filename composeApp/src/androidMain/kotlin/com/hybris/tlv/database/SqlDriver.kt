package com.hybris.tlv.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.applicationContext

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = AndroidSqliteDriver(
    schema = schema,
    context = applicationContext,
    name = if (inMemory) null else name
)
