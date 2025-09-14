package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.translation.model.Translation

internal interface TranslationUseCases {

    /**
     * Sync [Translation]s.
     */
    suspend fun syncTranslations()

    /**
     * Prepopulate [Translation]s.
     */
    suspend fun prepopulateTranslations()
}