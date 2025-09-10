package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.http.HttpClientFactory.Companion.TRANSLATIONS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.sync.model.SyncResult
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.domain.Translation
import database.AppDatabase
import io.ktor.client.HttpClient

internal class TranslationSync(
    private val dispatcher: Dispatcher,
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val translationDao = database.translationQueries

    suspend fun syncTranslations(): SyncResult =
        when (val result = httpClient.getStream<Translation>(path = TRANSLATIONS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteTranslations(translations = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateTranslations() {
        val translations = when {
            translationDao.isTranslationEmpty().executeAsList().isEmpty() -> loadFromJson<Translation>(
                path = "files/translations.json"
            ).also { rewriteTranslations(translations = it) }

            else -> translationDao.getTranslations().executeAsList().map { it.toTranslation() }
        }
        dispatcher.main.launch { TranslationCache.set(translations = translations) }
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
}