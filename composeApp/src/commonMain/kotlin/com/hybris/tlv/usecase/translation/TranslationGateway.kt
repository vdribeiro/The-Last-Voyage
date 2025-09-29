package com.hybris.tlv.usecase.translation

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.http.HttpClientFactory.Companion.TRANSLATIONS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Logger
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
                is Result.Error -> Logger.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                is Result.Success -> rewriteTranslations(translations = result.list)
            }
            config.localConfigs = config.localConfigs.copy(translationsVersion = config.remoteConfigs.translationsVersion)
        }
    }

    override suspend fun prepopulateTranslations() {
        if (translationDao.isTranslationEmpty().executeAsList().isEmpty()) {
            val translations: List<Translation> = loadFromJsonResource(path = TRANSLATIONS_JSON)
            rewriteTranslations(translations = translations)
            setCache(translations = translations)
        } else {
            val translations = translationDao.getTranslations().executeAsList().map { it.toTranslation() }
            setCache(translations = translations)
        }
    }

    private fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    private fun setCache(translations: List<Translation>) = dispatcher.main.launch {
        TranslationCache.set(translations = translations)
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