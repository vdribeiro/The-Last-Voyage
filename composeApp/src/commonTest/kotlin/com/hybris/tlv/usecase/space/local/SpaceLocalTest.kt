package com.hybris.tlv.usecase.space.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class SpaceLocalTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `write and get stellar hosts`() = runBlocking {
        assertTrue(actual = mock.spaceDao.isStellarHostEmpty())
        mock.spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        assertEquals(expected = stellarHosts, actual = mock.spaceDao.getStellarHosts())
    }

    @Test
    fun `write and get planets`() = runBlocking {
        assertTrue(actual = mock.spaceDao.isPlanetEmpty())
        mock.spaceDao.rewritePlanets(planets = planets)
        assertEquals(expected = planets, actual = mock.spaceDao.getPlanets())
    }
}
