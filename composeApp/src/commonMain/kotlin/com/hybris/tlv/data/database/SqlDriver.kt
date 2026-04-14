package com.hybris.tlv.data.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.hybris.tlv.data.database.DatabaseFactory.Companion.DATABASE_FILE
import com.hybris.tlv.test.ExcludeFromTesting
import database.AppDatabase

/**
 * Factory function to instantiate a platform-specific [SqlDriver].
 * This function is marked as `suspend` to support asynchronous driver initialization, which is a requirement for async-enabled drivers.
 *
 * @param name The filename of the database. Defaults to [DATABASE_FILE].
 * @param schema The SQLDelight [SqlSchema] used to create or migrate the database. Defaults to the auto-generated [AppDatabase.Schema].
 * @return A [SqlDriver] ready to be consumed by [DatabaseFactory].
 */
@ExcludeFromTesting
internal expect suspend fun createSqlDriver(
    name: String = DATABASE_FILE,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>> = AppDatabase.Schema
): SqlDriver
