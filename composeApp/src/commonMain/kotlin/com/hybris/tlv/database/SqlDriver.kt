package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver

internal expect fun createSqlDriver(inMemory: Boolean = false): SqlDriver
