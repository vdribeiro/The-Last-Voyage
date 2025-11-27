package com.hybris.tlv.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.flow.Dispatcher

internal suspend fun SqlDriver.clearDatabase() = withContext(context = Dispatcher.IO) {
    runCatching {
        val query = "SELECT name FROM sqlite_master WHERE type='table' " +
                "AND name!='sqlite_sequence' AND name!='android_metadata'"
        executeQuery(
            identifier = null,
            sql = query,
            mapper = { cursor ->
                QueryResult.Value(value = buildList {
                    while (cursor.next().value) add(cursor.getString(index = 0))
                })
            },
            parameters = 0,
            binders = null
        ).value.forEach { table ->
            execute(
                identifier = null,
                sql = "DELETE FROM $table;",
                parameters = 0,
                binders = null
            ).value
        }
    }
}
