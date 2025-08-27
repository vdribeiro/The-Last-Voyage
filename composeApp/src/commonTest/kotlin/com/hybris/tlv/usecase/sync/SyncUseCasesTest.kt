package com.hybris.tlv.usecase.sync

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.storage.ConfigKey
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

internal class SyncUseCasesTest {

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and sync`() = runBlocking {
        mock.useCases.sync.setup()
        val totalOperations = 8f

        val prepopulate = mock.useCases.sync.prepopulate().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = prepopulate[i])
        }
        assertEquals(expected = SyncResult.Success, actual = prepopulate.last())

        val noSync = mock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = noSync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = noSync.last())

        mock.config.apply {
            put(key = ConfigKey.TranslationsVersion, value = 0L)
            put(key = ConfigKey.CatastrophesVersion, value = 0L)
            put(key = ConfigKey.EnginesVersion, value = 0L)
            put(key = ConfigKey.StellarHostsVersion, value = 0L)
            put(key = ConfigKey.PlanetsVersion, value = 0L)
            put(key = ConfigKey.EventsVersion, value = 0L)
            put(key = ConfigKey.AchievementsVersion, value = 0L)
            put(key = ConfigKey.CreditsVersion, value = 0L)
        }

        val sync = mock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = sync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = sync.last())
    }

    @Test
    fun `get archive`() = runBlocking {
        val totalOperations = 6f
        val archive = mock.useCases.sync.getArchive().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = archive[i])
        }
        assertEquals(expected = SyncResult.Success, actual = archive.last())
    }

    @Test
    fun `get error`() = runBlocking {
        val totalOperations = 8f

        errorMock.config.apply {
            put(key = ConfigKey.TranslationsVersion, value = 0L)
            put(key = ConfigKey.CatastrophesVersion, value = 0L)
            put(key = ConfigKey.EnginesVersion, value = 0L)
            put(key = ConfigKey.StellarHostsVersion, value = 0L)
            put(key = ConfigKey.PlanetsVersion, value = 0L)
            put(key = ConfigKey.EventsVersion, value = 0L)
            put(key = ConfigKey.AchievementsVersion, value = 0L)
            put(key = ConfigKey.CreditsVersion, value = 0L)
        }

        val errorSync = errorMock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = errorSync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = errorSync.last())
    }
}
