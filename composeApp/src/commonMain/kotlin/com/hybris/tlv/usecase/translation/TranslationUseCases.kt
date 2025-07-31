package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.translation.model.domain.Translation
import kotlinx.coroutines.flow.Flow

internal interface TranslationUseCases {

    /**
     * Rewrites the local and remote [Translation] data.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Syncs the remote [Translation] data to local.
     */
    suspend fun syncTranslations(): Flow<SyncResult>

    /**
     * Prepopulate local [Translation].
     */
    suspend fun prepopulateTranslations()

    /**
     * Loads translations to cache given a desired [languageIso].
     */
    suspend fun loadTranslationsToCache(languageIso: String?): List<Translation>
}
