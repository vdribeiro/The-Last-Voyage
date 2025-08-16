package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver

internal expect object SqlDriverFactory {

    fun build(inMemory: Boolean = false): SqlDriver
}
