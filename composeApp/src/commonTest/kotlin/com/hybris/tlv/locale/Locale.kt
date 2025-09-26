@file:Suppress("unused")

package com.hybris.tlv.locale

import com.hybris.tlv.usecase.translation.TranslationCache

internal fun getLanguage(): String = TranslationCache.DEFAULT_LANGUAGE

internal fun getLocalDateTime(utc: String): String = utc
