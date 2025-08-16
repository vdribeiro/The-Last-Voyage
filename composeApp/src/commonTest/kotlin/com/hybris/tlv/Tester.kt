package com.hybris.tlv

import com.hybris.tlv.database.SqlDriverFactory
import com.hybris.tlv.http.json.loadFromJson
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.runBlocking

internal abstract class Tester {

    private val driver = SqlDriverFactory.build()

    val stellarHosts: List<StellarHost> by lazy {
        runBlocking { loadFromJson(path = "files/hosts.json") }
    }
    val planets: List<Planet> by lazy {
        runBlocking { loadFromJson(path = "files/planets.json") }
    }
}
