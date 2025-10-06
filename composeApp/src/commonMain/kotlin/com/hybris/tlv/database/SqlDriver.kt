package com.hybris.tlv.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import database.AppDatabase

internal expect fun createSqlDriver(
    name: String = "tlv_app.db",
    schema: SqlSchema<QueryResult.Value<Unit>> = AppDatabase.Schema,
    inMemory: Boolean = false
): SqlDriver
