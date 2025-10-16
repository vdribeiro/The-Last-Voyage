package com.hybris.tlv.database

import java.io.File
import java.util.Properties
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.platform.Property

internal actual fun createSqlDriver(
    name: String,
    schema: SqlSchema<QueryResult.Value<Unit>>,
    inMemory: Boolean
): SqlDriver = JdbcSqliteDriver(
    url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else "jdbc:sqlite:${getDatabasePath(name = name)}",
    properties = Properties(),
    schema = schema,
)

private fun getDatabasePath(name: String): String {
    val os = System.getProperty("os.name").lowercase()
    val baseDir = when {
        os.contains(other = "win") -> System.getenv("APPDATA")
        os.contains(other = "mac") -> System.getProperty("user.home") + "/Library/Application Support"
        else -> System.getProperty("user.home") + "/.local/share"
    }
    val appDir = File(baseDir, Property.APP_NAME)
    if (!appDir.exists()) appDir.mkdirs()
    return File(appDir, name).absolutePath
}
