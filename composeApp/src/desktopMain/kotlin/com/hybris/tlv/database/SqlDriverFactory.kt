package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.AppDatabase
import java.util.Properties

internal actual object SqlDriverFactory {
    actual fun build(inMemory: Boolean): SqlDriver =
        JdbcSqliteDriver(
            url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else "jdbc:sqlite:${Database.NAME}",
            properties = Properties(),
            schema = AppDatabase.Schema,
        )
}
