package com.hybris.tlv.database.adapter

import app.cash.sqldelight.ColumnAdapter

/**
 * A [ColumnAdapter] that allows the database to store a [Set] of [String]s in a single database column,
 * by converting the set to a single delimited [String], and splitting the string back into a set when reading from the database.
 */
internal object SetColumnAdapter: ColumnAdapter<Set<String>, String> {
    private const val SEPARATOR = ";,:."

    private fun <E> Collection<E>.encode(): String =
        joinToString(separator = SEPARATOR)

    private fun String.decode(): List<String> =
        if (isEmpty()) listOf() else split(SEPARATOR)

    override fun decode(databaseValue: String): Set<String> = databaseValue.decode().toSet()
    override fun encode(value: Set<String>): String = value.encode()
}
