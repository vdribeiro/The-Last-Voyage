package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = testCore.useCases.catastrophe.getRandomCatastrophe())
        testCore.useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = testCore.useCases.catastrophe.getRandomCatastrophe()).let {}
    }

    @Test
    fun `prepopulate and get catastrophes`() = runBlocking {
        assertNull(actual = testCore.useCases.catastrophe.getRandomCatastrophe())
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = testCore.useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
