package com.hybris.tlv.http

internal fun Boolean?.toBoolString(): String =
    this?.let { if (it) 1 else 0 }.toString()

internal fun Map<String, Any>.getString(key: String): String? =
    get(key = key)?.toString()

internal fun Map<String, Any>.getDouble(key: String): Double? =
    getString(key = key)?.toDoubleOrNull()

internal fun Map<String, Any>.getBoolean(key: String): Boolean? =
    getString(key = key)?.toBooleanStrictOrNull()
