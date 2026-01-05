package com.hybris.tlv.usecase.translation

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.TRANSLATIONS_URL
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.translation.model.Translation
import database.AppDatabase

internal class TranslationGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): TranslationUseCases {

    private val translationDao = database.translationQueries

    override suspend fun syncTranslations(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.getStream<Translation>(path = TRANSLATIONS_URL)) {
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
            val translations: List<Translation> = loadFromJsonResource(path = TRANSLATIONS_JSON)
            rewriteTranslations(translations = translations)
            true
        } else false
    }

    private fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override suspend fun refreshCache() {
        val translations = translationDao.getTranslations().executeAsList().map { it.toTranslation() }
        TranslationCache.set(translations = translations)
        Telemetry.info(tag = TAG, message = "Refreshed translations cache")
    }

    companion object {
        private const val TAG = "Translation"
    }
}