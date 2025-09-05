package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.mapper.toCartesian
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome
import kotlin.math.ceil
import kotlin.math.sqrt

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

    override suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost> {
        if (n <= 0) return emptyList()
        val stellarHostCP = stellarHost.toCartesian() ?: return emptyList()
        val nearest = mutableListOf<Pair<StellarHost, Double>>()
        getExoplanets()
            .asSequence()
            .filter { it.id != stellarHost.id && it.id !in visited }
            .forEach { otherStellarHost ->
                val otherStellarHostCP = otherStellarHost.toCartesian() ?: return@forEach
                val distanceSquared = stellarHostCP.distanceSquaredBetween(cp = otherStellarHostCP)
                when {
                    nearest.size < n -> {
                        nearest.add(otherStellarHost to distanceSquared)
                        nearest.sortBy { it.second }
                    }

                    else -> {
                        val farthestDistanceSquared = nearest.last().second
                        if (distanceSquared < farthestDistanceSquared) {
                            nearest[n - 1] = otherStellarHost to distanceSquared
                            nearest.sortBy { it.second }
                        }
                    }
                }
            }
        return nearest.map { (stellarHost, distanceSquared) ->
            val finalDistance = sqrt(x = distanceSquared)
            stellarHost.copy(distance = finalDistance).apply {
                planets.addAll(elements = stellarHost.planets)
                travelOutcome = TravelOutcome(fuel = ceil(x = finalDistance).toInt())
            }
        }
    }
}
