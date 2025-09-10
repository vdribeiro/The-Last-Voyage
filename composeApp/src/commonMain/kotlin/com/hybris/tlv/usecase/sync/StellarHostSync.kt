package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.StellarHostSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.STELLAR_HOSTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class StellarHostSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val stellarHostDao = database.stellarHostQueries

    suspend fun syncStellarHosts(): SyncResult =
        when (val result = httpClient.getStream<StellarHost>(path = STELLAR_HOSTS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteStellarHosts(stellarHosts = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateStellarHosts() {
        if (stellarHostDao.isStellarHostEmpty().executeAsList().isEmpty()) {
            val stellarHosts: List<StellarHost> = loadFromJson(path = "files/hosts.json")
            rewriteStellarHosts(stellarHosts = stellarHosts)
        }
    }

    private fun rewriteStellarHosts(stellarHosts: List<StellarHost>) = stellarHostDao.transaction {
        stellarHostDao.truncateStellarHost()
        stellarHosts.forEach { stellarHostDao.upsertStellarHost(StellarHost = it.toStellarHostSchema()) }
    }

    private fun StellarHost.toStellarHostSchema(): StellarHostSchema =
        com.hybris.tlv.database.StellarHostSchema(
            id = id,
            name = name,
            systemName = systemName,
            spectralType = spectralType,
            effectiveTemperature = effectiveTemperature,
            radius = radius,
            mass = mass,
            metallicity = metallicity,
            luminosity = luminosity,
            gravity = gravity,
            age = age,
            density = density,
            rotationalVelocity = rotationalVelocity,
            rotationalPeriod = rotationalPeriod,
            distance = distance,
            ra = ra,
            dec = dec
        )
}