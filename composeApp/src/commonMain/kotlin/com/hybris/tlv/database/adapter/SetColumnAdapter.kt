package com.hybris.tlv.database.adapter

import app.cash.sqldelight.ColumnAdapter

internal object SetColumnAdapter: ColumnAdapter<Set<String>, String> {
    private const val SEPARATOR = ";,:."

    private fun <E> Collection<E>.encode(): String =
        joinToString(separator = SEPARATOR)

    private fun String.decode(): List<String> =
        if (isEmpty()) listOf() else split(SEPARATOR)

    override fun decode(databaseValue: String): Set<String> = databaseValue.decode().toSet()
    override fun encode(value: Set<String>): String = value.encode()
}
