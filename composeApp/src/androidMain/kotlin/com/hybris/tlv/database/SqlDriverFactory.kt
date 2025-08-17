package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.applicationContext
import database.AppDatabase

internal actual object SqlDriverFactory {
    actual fun build(inMemory: Boolean): SqlDriver =
        AndroidSqliteDriver(
            context = applicationContext,
            schema = AppDatabase.Schema,
            name = if (inMemory) null else Database.NAME
        )
}
