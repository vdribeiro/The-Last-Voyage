package com.hybris.tlv.usecase.translation

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.DEFAULT_LANGUAGE
import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.resource.JsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.translation.model.Translation
import database.AppDatabase

internal class TranslationGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): TranslationUseCases {

    private val translationDao = database.translationQueries

    override suspend fun syncTranslations(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Translation>(path = URL.Translations)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteTranslations(translations = result.list)
                Telemetry.info(tag = TAG, message = "Successful translations sync")
                true
            }
        }
    }

    override suspend fun prepopulateTranslations(): Boolean = withContext(context = Dispatcher.IO) {
        if (translationDao.isTranslationEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating translations")
            val translations: List<Translation> = loadFromJsonResource(json = JsonResource.Translations)
            rewriteTranslations(translations = translations)
            true
        } else false
    }

    private fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override suspend fun refreshCache() {
        var languageIso = getLanguage()
        val translations = getTranslations(languageIso = languageIso).ifEmpty {
            languageIso = DEFAULT_LANGUAGE
            getTranslations(languageIso = languageIso)
        }
        TranslationCache.set(translations = translations, languageIso = languageIso)
        Telemetry.info(tag = TAG, message = "Refreshed translations cache")
    }

    private suspend fun getTranslations(languageIso: String): List<Translation> = withContext(context = Dispatcher.IO) {
        translationDao.getTranslations(languageIso = languageIso).executeAsList().map { it.toTranslation() }
    }

    companion object {
        private const val TAG = "Translation"
    }
}