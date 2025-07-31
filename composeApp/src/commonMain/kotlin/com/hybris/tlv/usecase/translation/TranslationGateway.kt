package com.hybris.tlv.usecase.translation

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.mapper.toTranslationCacheMap
import com.hybris.tlv.usecase.translation.model.domain.Translation
import com.hybris.tlv.usecase.translation.remote.TranslationRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class TranslationGateway(
    private val dispatcher: Dispatcher,
    private val translationApi: TranslationRemote,
    private val translationDao: TranslationLocal
): TranslationUseCases {

    private suspend fun loadTranslationsFromJson(): List<Translation> = runCatching {
        val jsonString = Res.readBytes(path = "files/translations.json").decodeToString()
        json.decodeFromString<List<Translation>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val translations = loadTranslationsFromJson()
        translationDao.rewriteTranslations(translations = translations)
        return translationApi.rewriteTranslations(translations = translations)
    }

    override suspend fun syncTranslations(): Flow<SyncResult> =
        translationApi.getTranslations(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateTranslations()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    translationDao.rewriteTranslations(translations = result.list)
                    val translationsMap = result.list.toTranslationCacheMap()
                    dispatcher.main.launch { TranslationCache.set(translations = translationsMap) }
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateTranslations() {
        if (translationDao.isTranslationEmpty()) {
            val translations = loadTranslationsFromJson()
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
