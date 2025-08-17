package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import database.AppDatabase

internal class IosSqlDriverFactory: SqlDriverFactory {

    override fun build(): SqlDriver =
        NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = Database.NAME,
        )
}
