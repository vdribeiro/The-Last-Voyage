package com.hybris.tlv.database

import java.io.File
import java.util.Properties
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.storage.appDataDir

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = JdbcSqliteDriver(
    url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else {
        "jdbc:sqlite:${File(appDataDir, name).absolutePath}"
    },
    properties = Properties(),
    schema = schema,
)
