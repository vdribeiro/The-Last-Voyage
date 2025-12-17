package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.translation.model.Translation

internal interface TranslationUseCases {

    /**
     * Sync [Translation]s.
     */
    suspend fun syncTranslations(): Boolean

    /**
     * Prepopulate [Translation]s.
     */
    suspend fun prepopulateTranslations()

    /**
     * Refresh translations cache.
     */
    suspend fun refreshCache()
}
