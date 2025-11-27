package com.hybris.tlv.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.JournalMode

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
