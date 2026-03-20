package com.hybris.tlv.domain.usecase.translation

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.DEFAULT_LANGUAGE
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.translation.model.Translation
import database.AppDatabase

internal class TranslationGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): TranslationUseCases {

    private val translationDao = database.translationQueries

    override suspend fun syncTranslations(): Boolean = withContext(context = Dispatcher.IO) {
        val translations = mutableListOf<Translation>()
        var syncResult = true
        when (val result = httpClient.get<Translation>(path = URL.Translations)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                syncResult = false
            }

            is Result.Success -> {
                translations.addAll(elements = result.list)
                Telemetry.info(tag = TAG, message = "Successful translations sync")
            }
        }
        when (val result = httpClient.get<Translation>(path = URL.CatastrophesTranslations)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                syncResult = false
            }

            is Result.Success -> {
                translations.addAll(elements = result.list)
                Telemetry.info(tag = TAG, message = "Successful translations sync")
            }
        }
        when (val result = httpClient.get<Translation>(path = URL.EnginesTranslations)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                syncResult = false
            }

            is Result.Success -> {
                translations.addAll(elements = result.list)
                Telemetry.info(tag = TAG, message = "Successful translations sync")
            }
        }
        when (val result = httpClient.get<Translation>(path = URL.EventsTranslations)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                syncResult = false
            }

            is Result.Success -> {
                translations.addAll(elements = result.list)
                Telemetry.info(tag = TAG, message = "Successful translations sync")
            }
        }

        rewriteTranslations(translations = translations)
        return@withContext syncResult
    }

    override suspend fun prepopulateTranslations(): Boolean = withContext(context = Dispatcher.IO) {
        if (translationDao.isTranslationEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating translations")
            val translations: List<Translation> = loadAllTranslationsFromJsonResource()
            rewriteTranslations(translations = translations)
            true
        } else false
    }

    private suspend fun rewriteTranslations(translations: List<Translation>) = translationDao.transactionWithResult {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override suspend fun getTranslations(languageIso: String): List<Translation> = withContext(context = Dispatcher.IO) {
        val translations = translationDao.getTranslations(languageIso = languageIso).awaitAsList().map { it.toTranslation() }
        if (translations.isEmpty() && languageIso != DEFAULT_LANGUAGE) getTranslations(languageIso = DEFAULT_LANGUAGE) else translations
    }

    companion object {
        private const val TAG = "Translation"

        suspend fun loadAllTranslationsFromJsonResource(): List<Translation> =
            loadFromJsonResource<Translation>(json = JsonResource.Translations) +
                    loadFromJsonResource<Translation>(json = JsonResource.CatastrophesTranslations) +
                    loadFromJsonResource<Translation>(json = JsonResource.EnginesTranslations) +
                    loadFromJsonResource<Translation>(json = JsonResource.EventsTranslations) +
                    loadFromJsonResource<Translation>(json = JsonResource.AchievementsTranslations)
    }
}