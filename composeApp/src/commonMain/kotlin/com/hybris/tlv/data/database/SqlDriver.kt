@file:ExcludeFromTesting

package com.hybris.tlv.data.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.hybris.tlv.data.database.DatabaseFactory.Companion.DATABASE_FILE
import com.hybris.tlv.test.ExcludeFromTesting
import database.AppDatabase

/**
 * Creates the database driver.
 */
internal expect suspend fun createSqlDriver(
    name: String = DATABASE_FILE,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>> = AppDatabase.Schema
): SqlDriver
