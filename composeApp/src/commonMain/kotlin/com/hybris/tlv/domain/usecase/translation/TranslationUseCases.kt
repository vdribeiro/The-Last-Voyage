package com.hybris.tlv.domain.usecase.translation

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
     * Refresh translations cache.
     */
    suspend fun refreshCache()
}
