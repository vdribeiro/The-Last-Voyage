package com.hybris.tlv.usecase.space.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class SpaceLocalTest: Tester() {

    @Test
    fun `write and get stellar hosts`() = runBlocking {
        assertTrue(actual = spaceDao.isStellarHostEmpty())
        spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        assertEquals(expected = stellarHosts, actual = spaceDao.getStellarHosts())
    }

    @Test
    fun `write and get planets`() = runBlocking {
        assertTrue(actual = spaceDao.isPlanetEmpty())
        spaceDao.rewritePlanets(planets = planets)
        assertEquals(expected = planets, actual = spaceDao.getPlanets())
    }
}
