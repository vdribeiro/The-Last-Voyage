package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.sync.model.SyncResult
import com.hybris.tlv.usecase.translation.model.domain.Translation

internal interface TranslationInternalUseCases {

    /**
     * Syncs the remote [Translation] data to local.
     */
    suspend fun syncTranslations(): SyncResult

    /**
     * Prepopulate local [Translation].
     */
    suspend fun prepopulateTranslations()

    /**
     * Loads translations to cache given a desired [languageIso].
     */
    suspend fun loadTranslationsToCache(languageIso: String?): List<Translation>
}
