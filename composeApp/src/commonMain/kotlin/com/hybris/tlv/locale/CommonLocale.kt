package com.hybris.tlv.locale

import com.hybris.tlv.usecase.translation.TranslationCache

internal class CommonLocale: Locale {

    override fun getLanguage(): String = TranslationCache.DEFAULT_LANGUAGE

    override fun getLocalDateTime(utc: String): String = utc
}
