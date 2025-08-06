package com.hybris.tlv.usecase.translation.local

import com.hybris.tlv.usecase.translation.mapper.toTranslation
import com.hybris.tlv.usecase.translation.mapper.toTranslationSchema
import com.hybris.tlv.usecase.translation.model.domain.Translation
import database.AppDatabase

internal class TranslationDao(
    database: AppDatabase
): TranslationLocal {

    private val translationDao = database.translationQueries

    override fun isTranslationEmpty(): Boolean =
        translationDao.isTranslationEmpty().executeAsList().isEmpty()

    override fun rewriteTranslations(translations: List<Translation>) = translationDao.transaction {
        translationDao.truncateTranslation()
        translations.forEach { translationDao.upsertTranslation(Translation = it.toTranslationSchema()) }
    }

    override fun getTranslations(): List<Translation> =
        translationDao.getTranslations().executeAsList().map { it.toTranslation() }
}
