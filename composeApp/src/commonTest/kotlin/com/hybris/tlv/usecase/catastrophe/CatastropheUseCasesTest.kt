package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = mockCore.useCases.catastrophe.getRandomCatastrophe())
        mockCore.useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = mockCore.useCases.catastrophe.getRandomCatastrophe()).let {}
    }

    @Test
    fun `prepopulate and get catastrophes`() = runBlocking {
        assertNull(actual = mockCore.useCases.catastrophe.getRandomCatastrophe())
        mockCore.useCases.catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = mockCore.useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
