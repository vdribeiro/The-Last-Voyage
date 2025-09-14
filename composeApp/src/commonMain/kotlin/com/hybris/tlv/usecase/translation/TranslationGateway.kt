package com.hybris.tlv.usecase.translation

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.http.HttpClientFactory.Companion.TRANSLATIONS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.translation.model.Translation
import database.AppDatabase
import io.ktor.client.HttpClient

internal class TranslationGateway(
    private val dispatcher: Dispatcher,
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): TranslationUseCases {

    private val translationDao = database.translationQueries

    override suspend fun syncTranslations() {
        if (config.remoteConfigs.translationsVersion > config.localConfigs.translationsVersion) {
            when (val result = httpClient.getStream<Translation>(path = TRANSLATIONS_URL)) {
                is Result.Error -> Logger.error(tag = TAG, message = result.error)
                is Result.Success -> rewriteTranslations(translations = result.list)
            }
        }
    }

    override suspend fun prepopulateTranslations() {
        if (translationDao.isTranslationEmpty().executeAsList().isEmpty()) {
            val translations: List<Translation> = loadFromJson(path = "files/translations.json")
            rewriteTranslations(translations = translations)
            dispatcher.main.launch { TranslationCache.set(translations = translations) }
        } else {
            val translations = translationDao.getTranslations().executeAsList().map { it.toTranslation() }
            dispatcher.main.launch { TranslationCache.set(translations = translations) }
        }
    }

    private fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    private fun Translation.toTranslationSchema(): TranslationSchema =
        com.hybris.tlv.database.TranslationSchema(
            languageIso = languageIso,
            key = key,
            value_ = value
        )

    private fun TranslationSchema.toTranslation(): Translation =
        Translation(
            languageIso = languageIso,
            key = key,
            value = value_
        )

    companion object {
        private const val TAG = "Translation"
    }
}