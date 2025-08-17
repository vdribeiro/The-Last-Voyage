package com.hybris.tlv.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import database.AppDatabase

internal class AndroidSqlDriverFactory(private val context: Context): SqlDriverFactory {

    override fun build(): SqlDriver =
        AndroidSqliteDriver(
            context = context,
            schema = AppDatabase.Schema,
            name = Database.NAME
        )
}
