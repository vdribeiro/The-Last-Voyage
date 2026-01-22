package com.hybris.tlv.domain.usecase.translation

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.DEFAULT_LANGUAGE
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.infrastructure.resource.JsonResource
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

    private suspend fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override suspend fun getTranslations(languageIso: String): List<Translation> = withContext(context = Dispatcher.IO) {
        val translations = translationDao.getTranslations(languageIso = languageIso).executeAsList().map { it.toTranslation() }
        if (translations.isEmpty() && languageIso != DEFAULT_LANGUAGE) getTranslations(languageIso = DEFAULT_LANGUAGE) else translations
    }

    companion object {
        private const val TAG = "Translation"
    }
}