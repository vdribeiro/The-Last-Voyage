package com.hybris.tlv.usecase.earth.local

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthLocalTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `write and get catastrophes`() = runBlocking {
        assertTrue(actual = mock.earthDao.isCatastropheEmpty())
        mock.earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        assertNotNull(actual = mock.earthDao.getRandomCatastrophe())
    }
}
