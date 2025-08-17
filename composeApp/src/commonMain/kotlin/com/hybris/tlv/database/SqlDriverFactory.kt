package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver

internal interface SqlDriverFactory {

    fun build(): SqlDriver
}
