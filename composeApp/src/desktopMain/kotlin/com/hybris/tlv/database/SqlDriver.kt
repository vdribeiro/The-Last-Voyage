package com.hybris.tlv.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.AppDatabase
import java.io.File
import java.util.Properties

internal actual fun createSqlDriver(inMemory: Boolean): SqlDriver =
    JdbcSqliteDriver(
        url = if (inMemory) JdbcSqliteDriver.IN_MEMORY else "jdbc:sqlite:${getDatabasePath()}",
        properties = Properties(),
        schema = AppDatabase.Schema,
    )
private fun getDatabasePath(): String {
    val appName = "The Last Voyage"
    val os = System.getProperty("os.name").lowercase()
    val baseDir = when {
        os.contains(other = "win") -> System.getenv("APPDATA")
        os.contains(other = "mac") -> System.getProperty("user.home") + "/Library/Application Support"
        else -> System.getProperty("user.home") + "/.local/share"
    }
    val appDir = File(baseDir, appName)
    if (!appDir.exists()) appDir.mkdirs()
    return File(appDir, Database.NAME).absolutePath
}
