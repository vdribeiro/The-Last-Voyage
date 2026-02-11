@file:ExcludeFromTesting

package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.JournalMode
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.test.ExcludeFromTesting

internal actual suspend fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver = withContext(context = Dispatcher.IO) {
    NativeSqliteDriver(
        schema = schema.synchronous(),
        name = name,
        onConfiguration = { config ->
            config.copy(
                journalMode = JournalMode.WAL
            )
        }
    )
}
