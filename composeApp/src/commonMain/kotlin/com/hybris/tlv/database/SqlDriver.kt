package com.hybris.tlv.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.hybris.tlv.database.DatabaseFactory.Companion.DATABASE_FILE
import database.AppDatabase

/**
 * Creates the database driver.
 */
internal expect fun createSqlDriver(
    name: String = DATABASE_FILE,
    schema: SqlSchema<QueryResult.Value<Unit>> = AppDatabase.Schema,
    inMemory: Boolean = false
): SqlDriver
