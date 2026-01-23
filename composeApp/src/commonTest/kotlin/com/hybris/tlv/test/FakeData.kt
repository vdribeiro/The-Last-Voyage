package com.hybris.tlv.test

import com.hybris.tlv.core.flow.runBlocking
import com.hybris.tlv.data.config.Configs
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.domain.usecase.credit.model.Credit
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.domain.usecase.space.addPlanets
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.infrastructure.resource.JsonResource

internal val configs: Configs by lazy {
    Configs(
        appVersion = Long.MAX_VALUE,
        translationsVersion = Long.MAX_VALUE,
        catastrophesVersion = Long.MAX_VALUE,
        enginesVersion = Long.MAX_VALUE,
        stellarHostsVersion = Long.MAX_VALUE,
        planetsVersion = Long.MAX_VALUE,
        eventsVersion = Long.MAX_VALUE,
        achievementsVersion = Long.MAX_VALUE,
        creditsVersion = Long.MAX_VALUE,
        developerCorner = "",
        formula = "",
    )
}

internal val translations: List<Translation> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Translations) }
}
internal val catastrophes: List<Catastrophe> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Catastrophes) }
}
internal val engines: List<Engine> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Engines) }
}
internal val events: List<Event> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Events) }
}
internal val achievements: List<Achievement> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Achievements) }
}
internal val credits: List<Credit> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Credits) }
}
internal val stellarHosts: List<StellarHost> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.StellarHosts) }
}
internal val planets: List<Planet> by lazy {
    runBlocking { loadFromJsonResource(json = JsonResource.Planets) }
}
internal val hostsWithPlanets: List<StellarHost> by lazy {
    stellarHosts.addPlanets(planets = planets)
}
internal val shipPrototype: ShipPrototype by lazy {
    ShipPrototype(
        assignedPoints = 10,
        sensorRange = 5,
        fuel = 100,
        materials = 90,
        cryopods = 150,
    )
}
internal val ship: Ship by lazy {
    Ship(
        id = "1",
        engine = engines.random(),
        assignedPoints = 10,
        yearsTraveled = 100.0,
        sensorRange = 5,
        integrity = 80,
        fuel = 100,
        materials = 90,
        cryopods = 150,
    )
}
internal val gameSessionPrototype: GameSessionPrototype by lazy {
    GameSessionPrototype(
        ship = shipPrototype,
        engine = engines.random(),
        formula = Formula()
    )
}
