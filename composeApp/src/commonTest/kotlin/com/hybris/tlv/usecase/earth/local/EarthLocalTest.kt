package com.hybris.tlv.usecase.earth.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthLocalTest {

    private val mock = Mock()

    @Test
    fun `write and get catastrophes`() = runBlocking {
        assertTrue(actual = mock.earthDao.isCatastropheEmpty())
        mock.earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        assertEquals(expected = catastrophes, actual = mock.earthDao.getCatastrophes())
    }
}
