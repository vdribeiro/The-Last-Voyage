package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = testDependency.useCases.catastrophe.getRandomCatastrophe())
        testDependency.useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = testDependency.useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
