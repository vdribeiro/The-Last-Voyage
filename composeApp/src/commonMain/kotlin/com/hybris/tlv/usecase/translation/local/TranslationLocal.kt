package com.hybris.tlv.usecase.translation.local

import com.hybris.tlv.usecase.translation.model.domain.Translation

internal interface TranslationLocal {

    /**
     * Returns true if there are no [Translation]s in the database, false otherwise.
     */
    fun isTranslationEmpty(): Boolean

    /**
     * Rewrites the [Translation] table with the given [translations].
     */
    fun rewriteTranslations(translations: List<Translation>)

    /**
     * Get all [Translation]s.
     */
    fun getTranslations(): List<Translation>
}
