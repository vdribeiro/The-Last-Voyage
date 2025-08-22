package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.http.TRANSLATIONS_URL
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.translation.model.domain.Translation
import io.ktor.client.HttpClient

internal class TranslationApi(
    private val httpClient: HttpClient
): TranslationRemote {

    override suspend fun getTranslations(): Result<Translation> =
        httpClient.getStream(path = TRANSLATIONS_URL)
}
