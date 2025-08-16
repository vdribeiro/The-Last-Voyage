package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.applicationContext
import database.AppDatabase
import java.util.Properties

internal actual object SqlDriverFactory {
    actual fun build(inMemory: Boolean): SqlDriver =
        when (inMemory) {
            true -> JdbcSqliteDriver(
                url = JdbcSqliteDriver.IN_MEMORY,
                properties = Properties(),
                schema = AppDatabase.Schema
            )

            false -> AndroidSqliteDriver(
                context = applicationContext,
                schema = AppDatabase.Schema,
                name = Database.NAME
            )
        }
}
