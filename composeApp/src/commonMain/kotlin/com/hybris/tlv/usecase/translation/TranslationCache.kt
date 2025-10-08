package com.hybris.tlv.usecase.translation

import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.platform.Property
import com.hybris.tlv.usecase.translation.model.Translation
import kotlin.concurrent.Volatile

/**
 * Translations cache.
 */
internal object TranslationCache {

    const val DEFAULT_LANGUAGE = "en"
    @Volatile
    private var languageIso: String = DEFAULT_LANGUAGE
    @Volatile
    private var translationsCache: Map<String, String> = listOf(
        Translation(
            languageIso = "en",
            key = "app_name",
            value = Property.APP_NAME
        ),
        Translation(
            languageIso = "en",
            key = "splash_screen__loading",
            value = "Loading..."
        ),
    ).toTranslationCacheMap()

    fun set(translations: List<Translation>) {
        languageIso = getLanguage()
        translationsCache = translations.toTranslationCacheMap()
    }

    fun get(key: String): String =
        translationsCache["${languageIso}__${key}"] ?: if (languageIso != DEFAULT_LANGUAGE) {
            translationsCache["${DEFAULT_LANGUAGE}__${key}"] ?: key
        } else key

    fun get(key: String, vararg args: String): String {
        var translation = translationsCache["${languageIso}__$key"] ?: if (languageIso != DEFAULT_LANGUAGE) {
            translationsCache["${DEFAULT_LANGUAGE}__$key"] ?: return key
        } else return key
        args.forEachIndexed { index, arg -> translation = translation.replace(oldValue = "%${index + 1}\$s", newValue = arg) }
        return translation
    }

    private fun List<Translation>.toTranslationCacheMap(): Map<String, String> =
        associate { "${it.languageIso}__${it.key}" to it.value }
}

fun getTranslation(key: String, vararg args: String): String = TranslationCache.get(key = key, *args)
