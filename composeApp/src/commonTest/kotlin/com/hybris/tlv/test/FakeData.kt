package com.hybris.tlv.test

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

internal object FakeData {
    val configs: Configs by lazy {
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

    private var _translations = LazyData { loadFromJsonResource<Translation>(json = JsonResource.Translations) }
    suspend fun getTranslations(): List<Translation> = _translations.getData().orEmpty()

    private val _catastrophes = LazyData { loadFromJsonResource<Catastrophe>(json = JsonResource.Catastrophes) }
    suspend fun getCatastrophes(): List<Catastrophe> = _catastrophes.getData().orEmpty()

    private val _engines = LazyData { loadFromJsonResource<Engine>(json = JsonResource.Engines) }
    suspend fun getEngines(): List<Engine> = _engines.getData().orEmpty()

    private val _events = LazyData { loadFromJsonResource<Event>(json = JsonResource.Events) }
    suspend fun getEvents(): List<Event> = _events.getData().orEmpty()

    private val _achievements = LazyData { loadFromJsonResource<Achievement>(json = JsonResource.Achievements) }
    suspend fun getAchievements(): List<Achievement> = _achievements.getData().orEmpty()

    private val _credits = LazyData { loadFromJsonResource<Credit>(json = JsonResource.Credits) }
    suspend fun getCredits(): List<Credit> = _credits.getData().orEmpty()

    private val _stellarHosts = LazyData { loadFromJsonResource<StellarHost>(json = JsonResource.StellarHosts) }
    suspend fun getStellarHosts(): List<StellarHost> = _stellarHosts.getData().orEmpty()

    private val _planets = LazyData { loadFromJsonResource<Planet>(json = JsonResource.Planets) }
    suspend fun getPlanets(): List<Planet> = _planets.getData().orEmpty()

    private val _stellarHostsWithPlanets = LazyData { getStellarHosts().addPlanets(planets = getPlanets()) }
    suspend fun getHostsWithPlanets(): List<StellarHost> = _stellarHostsWithPlanets.getData().orEmpty()

    val shipPrototype: ShipPrototype by lazy {
        ShipPrototype(
            assignedPoints = 10,
            sensorRange = 5,
            fuel = 100,
            materials = 90,
            cryopods = 150,
        )
    }

    private val _ship = LazyData {
        Ship(
            id = "1",
            engine = getEngines().random(),
            assignedPoints = 10,
            yearsTraveled = 100.0,
            sensorRange = 5,
            integrity = 80,
            fuel = 100,
            materials = 90,
            cryopods = 150,
        )
    }

    suspend fun getShip(): Ship = _ship.getData()!!

    private val _gameSessionPrototype = LazyData {
        GameSessionPrototype(
            ship = shipPrototype,
            engine = getEngines().random(),
            formula = Formula()
        )
    }

    suspend fun getGameSessionPrototype(): GameSessionPrototype = _gameSessionPrototype.getData()!!
}
