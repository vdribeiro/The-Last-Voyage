package com.hybris.tlv.usecase.translation

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.mapper.toTranslationCacheMap
import com.hybris.tlv.usecase.translation.model.domain.Translation
import com.hybris.tlv.usecase.translation.remote.TranslationRemote

internal class TranslationInternalGateway(
    private val dispatcher: Dispatcher,
    private val translationApi: TranslationRemote,
    private val translationDao: TranslationLocal
): TranslationInternalUseCases {

    override suspend fun syncTranslations(): SyncResult =
        when (val result = translationApi.getTranslations()) {
            is Result.Error -> {
                prepopulateTranslations()
                SyncResult.Error(error = result.error)
            }

            is Result.Success -> {
                translationDao.rewriteTranslations(translations = result.list)
                val translationsMap = result.list.toTranslationCacheMap()
                dispatcher.main.launch { TranslationCache.set(translations = translationsMap) }
                SyncResult.Success
            }
        }

    override suspend fun prepopulateTranslations() {
        if (translationDao.isTranslationEmpty()) {
            val translations: List<Translation> = loadFromJson(path = "files/translations.json")
            translationDao.rewriteTranslations(translations = translations)
        }
    }

    override suspend fun loadTranslationsToCache(languageIso: String?): List<Translation> {
        val translations = translationDao.getTranslations()
        val translationsMap = translations.toTranslationCacheMap()
        dispatcher.main.launch {
            TranslationCache.set(languageIso = languageIso)
            TranslationCache.set(translations = translationsMap)
        }
        return translations
    }
}
