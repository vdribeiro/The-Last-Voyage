package com.hybris.tlv.domain.usecase.translation

import com.hybris.tlv.core.locale.DEFAULT_LANGUAGE
import com.hybris.tlv.core.locale.getLanguage
import com.hybris.tlv.domain.usecase.translation.model.Translation

internal interface TranslationUseCases {

    /**
     * Sync [Translation]s.
     */
    suspend fun syncTranslations(): Boolean

    /**
     * Prepopulate [Translation]s.
     */
    suspend fun prepopulateTranslations(): Boolean

    /**
     * Get translations for a specific language.
     * If the language is not supported, the [DEFAULT_LANGUAGE] is used.
     */
    suspend fun getTranslations(languageIso: String = getLanguage()): List<Translation>
}
