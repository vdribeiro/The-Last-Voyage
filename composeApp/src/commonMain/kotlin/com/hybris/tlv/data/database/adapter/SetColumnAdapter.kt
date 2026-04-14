package com.hybris.tlv.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import com.hybris.tlv.data.database.adapter.SetColumnAdapter.SEPARATOR

/**
 * A [ColumnAdapter] that serializes a [Set] of [String] values into a single delimited database column.
 * This adapter is essential where a collection needs to be stored without creating a separate relational table.
 * It uses a custom multi-character [SEPARATOR] to minimize the risk of collisions with standard user input.
 */
internal object SetColumnAdapter: ColumnAdapter<Set<String>, String> {

    /**
     * The delimiter used to join and split the string set.
     * Chosen as a complex sequence to avoid splitting on common punctuation found within the strings themselves.
     */
    private const val SEPARATOR = ";,:."

    /**
     * Extension to convert a collection into a delimited string.
     */
    private fun <E> Collection<E>.encode(): String =
        joinToString(separator = SEPARATOR)

    /**
     * Extension to convert a delimited string back into a list.
     * Handles empty strings by returning an empty list instead of a list containing an empty string.
     */
    private fun String.decode(): List<String> =
        if (isEmpty()) listOf() else split(SEPARATOR)

    override fun decode(databaseValue: String): Set<String> = databaseValue.decode().toSet()
    override fun encode(value: Set<String>): String = value.encode()
}