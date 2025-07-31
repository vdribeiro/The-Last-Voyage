package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.translation.mapper.toTranslation
import com.hybris.tlv.usecase.translation.mapper.toTranslationMap
import com.hybris.tlv.usecase.translation.model.domain.Translation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class TranslationApi(
    private val firestore: Firestore
): TranslationRemote {

    override suspend fun rewriteTranslations(translations: List<Translation>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = TRANSLATIONS).last()
        firestore.setCollection(collection = TRANSLATIONS, documents = translations.map { it.toTranslationMap() }).map { result ->
            when (result) {
                is FirestoreWriteResult.Error -> SyncResult.Error(error = result.error)
                is FirestoreWriteResult.PartialSuccess -> SyncResult.Loading(
                    progress = result.documents.size.toFloat(),
                    total = result.totalDocuments.toFloat()
                )

                is FirestoreWriteResult.Success -> SyncResult.Success
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun getTranslations(queryMap: QueryMap): Flow<Result<Translation>> = runCatching {
        firestore.getCollection(collection = TRANSLATIONS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toTranslation() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toTranslation() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object {
        private const val TAG = "TranslationApi"
        private const val TRANSLATIONS = "translations"
        const val TRANSLATION_ID = "id"
        const val TRANSLATION_ISO = "language_iso"
        const val TRANSLATION_KEY = "key"
        const val TRANSLATION_VALUE = "value"
    }
}
