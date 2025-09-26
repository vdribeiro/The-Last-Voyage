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
): SqlDriver = when (inMemory) {
    true -> AndroidSqliteDriver(
        context = applicationContext,
        schema = schema
    )

    false -> AndroidSqliteDriver(
        context = applicationContext,
        name = name,
        schema = schema,
    )
}
