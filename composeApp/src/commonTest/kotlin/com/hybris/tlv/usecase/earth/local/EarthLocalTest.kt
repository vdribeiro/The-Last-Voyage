package com.hybris.tlv.usecase.earth.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.catastrophes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthLocalTest: Tester() {

    @Test
    fun `write and get catastrophes`() = runBlocking {
        assertTrue(actual = earthDao.isCatastropheEmpty())
        earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        assertEquals(expected = catastrophes, actual = earthDao.getCatastrophes())
    }
}
