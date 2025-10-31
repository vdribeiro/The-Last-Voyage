package com.hybris.tlv.usecase.translation

import io.ktor.client.HttpClient
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.TRANSLATIONS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.translation.model.Translation
import database.AppDatabase

internal class TranslationGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): TranslationUseCases {

    private val translationDao = database.translationQueries

    override suspend fun syncTranslations() {
        val remoteVersion = config.remoteConfigs.translationsVersion
        val localVersion = config.localConfigs.translationsVersion
        Telemetry.info(tag = TAG, message = "Syncing translations: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Translation>(path = TRANSLATIONS_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get translations", throwable = result.error)
                is Result.Success -> {
                    rewriteTranslations(translations = result.list)
                    config.setConfigs { it.copy(translationsVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful translations sync")
                    return
                }
            }
        }
        if (translationDao.isTranslationEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating translations")
            val translations: List<Translation> = loadFromJsonResource(path = TRANSLATIONS_JSON)
            rewriteTranslations(translations = translations)
        }
    }

    private fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override suspend fun refreshCache() {
        val translations = translationDao.getTranslations().executeAsList().map { it.toTranslation() }
        TranslationCache.set(translations = translations)
    }

    private fun Translation.toTranslationSchema(): TranslationSchema =
        TranslationSchema(
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