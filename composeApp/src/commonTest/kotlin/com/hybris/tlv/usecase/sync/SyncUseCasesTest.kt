package com.hybris.tlv.usecase.sync

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

internal class SyncUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
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
            put(key = Configs.TranslationsVersion, value = 0L)
            put(key = Configs.CatastrophesVersion, value = 0L)
            put(key = Configs.EnginesVersion, value = 0L)
            put(key = Configs.StellarHostsVersion, value = 0L)
            put(key = Configs.PlanetsVersion, value = 0L)
            put(key = Configs.EventsVersion, value = 0L)
            put(key = Configs.AchievementsVersion, value = 0L)
            put(key = Configs.CreditsVersion, value = 0L)
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
            put(key = Configs.TranslationsVersion, value = 0L)
            put(key = Configs.CatastrophesVersion, value = 0L)
            put(key = Configs.EnginesVersion, value = 0L)
            put(key = Configs.StellarHostsVersion, value = 0L)
            put(key = Configs.PlanetsVersion, value = 0L)
            put(key = Configs.EventsVersion, value = 0L)
            put(key = Configs.AchievementsVersion, value = 0L)
            put(key = Configs.CreditsVersion, value = 0L)
        }

        val errorSync = errorMock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = errorSync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = errorSync.last())
    }
}
