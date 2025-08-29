package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.mapper.toCartesian
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome
import kotlin.math.ceil

internal class SpaceGateway(
    private val spaceDao: SpaceLocal,
): SpaceUseCases {

    override suspend fun getStellarHost(id: String): StellarHost? {
        val planets = spaceDao.getPlanetsByStellarHost(stellarHostId = id)
        return spaceDao.getStellarHost(id = id)?.apply { this.planets.addAll(elements = planets) }
    }

    override suspend fun getExoplanets(): List<StellarHost> {
        val planetMap = spaceDao.getPlanets().groupBy { it.stellarHostId }
        return spaceDao.getStellarHosts().apply {
            forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) }
        }
    }

    // TODO - improve performance
    override suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost> {
        val stellarHostCP = stellarHost.toCartesian() ?: return emptyList()
        return getExoplanets()
            .asSequence()
            .filter { it.id != stellarHost.id && it.id !in visited }
            .mapNotNull { other ->
                val otherCP = other.toCartesian() ?: return@mapNotNull null
                val distance = stellarHostCP.distanceBetween(cp = otherCP)
                Pair(first = other, second = distance)
            }
            .sortedBy { it.second }
            .take(n = n)
            .map {
                it.first.copy(distance = it.second).apply {
                    planets.addAll(elements = it.first.planets)
                    travelOutcome = TravelOutcome(fuel = ceil(x = it.second).toInt())
                }
            }
            .toList()
    }
}
