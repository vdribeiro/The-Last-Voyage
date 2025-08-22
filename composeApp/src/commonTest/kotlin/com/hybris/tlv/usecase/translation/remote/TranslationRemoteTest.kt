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

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

    @Test
    fun `get translations`() = runBlocking {
        assertEquals(expected = Result.Success(list = translations), actual = mock.translationApi.getTranslations())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.translationApi.getTranslations() is Result.Error)
    }
}
