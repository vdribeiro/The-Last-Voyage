package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.translations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class TranslationRemoteTest {

    @Test
    fun `get translations`() = runBlocking {
        assertEquals(expected = Result.Success(list = translations), actual = Mock().translationApi.getTranslations())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = Mock(httpClient = HttpClientFactory.buildErrorHttpClient()).translationApi.getTranslations() is Result.Error)
    }
}
