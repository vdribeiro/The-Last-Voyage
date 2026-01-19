@file:Suppress("unused")

package com.hybris.tlv.core.locale

internal fun getLanguage(): String = DEFAULT_LANGUAGE

internal fun getLocalDateTime(utc: String = now()): String = utc

internal fun observeLocaleChanges(onChanged: () -> Unit): Boolean = true
