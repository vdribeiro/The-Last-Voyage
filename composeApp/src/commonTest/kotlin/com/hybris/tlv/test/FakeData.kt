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

    private var _translations: List<Translation>? = null
    suspend fun getTranslations(): List<Translation> =
        _translations ?: loadFromJsonResource<Translation>(json = JsonResource.Translations).also { _translations = it }

    private var _catastrophes: List<Catastrophe>? = null
    suspend fun getCatastrophes(): List<Catastrophe> =
        _catastrophes ?: loadFromJsonResource<Catastrophe>(json = JsonResource.Catastrophes).also { _catastrophes = it }

    private var _engines: List<Engine>? = null
    suspend fun getEngines(): List<Engine> =
        _engines ?: loadFromJsonResource<Engine>(json = JsonResource.Engines).also { _engines = it }

    private var _events: List<Event>? = null
    suspend fun getEvents(): List<Event> =
        _events ?: loadFromJsonResource<Event>(json = JsonResource.Events).also { _events = it }

    private var _achievements: List<Achievement>? = null
    suspend fun getAchievements(): List<Achievement> =
        _achievements ?: loadFromJsonResource<Achievement>(json = JsonResource.Achievements).also { _achievements = it }

    private var _credits: List<Credit>? = null
    suspend fun getCredits(): List<Credit> =
        _credits ?: loadFromJsonResource<Credit>(json = JsonResource.Credits).also { _credits = it }

    private var _stellarHosts: List<StellarHost>? = null
    suspend fun getStellarHosts(): List<StellarHost> =
        _stellarHosts ?: loadFromJsonResource<StellarHost>(json = JsonResource.StellarHosts).also { _stellarHosts = it }

    private var _planets: List<Planet>? = null
    suspend fun getPlanets(): List<Planet> =
        _planets ?: loadFromJsonResource<Planet>(json = JsonResource.Planets).also { _planets = it }

    private var _stellarHostsWithPlanets: List<StellarHost>? = null
    suspend fun getHostsWithPlanets(): List<StellarHost> =
        _stellarHostsWithPlanets ?: getStellarHosts().addPlanets(planets = getPlanets()).also { _stellarHostsWithPlanets = it }

    val shipPrototype: ShipPrototype by lazy {
        ShipPrototype(
            assignedPoints = 10,
            sensorRange = 5,
            fuel = 100,
            materials = 90,
            cryopods = 150,
        )
    }

    private var _ship: Ship? = null
    suspend fun getShip(): Ship = _ship ?: Ship(
        id = "1",
        engine = getEngines().random(),
        assignedPoints = 10,
        yearsTraveled = 100.0,
        sensorRange = 5,
        integrity = 80,
        fuel = 100,
        materials = 90,
        cryopods = 150,
    ).also { _ship = it }

    private var _gameSessionPrototype: GameSessionPrototype? = null
    suspend fun getGameSessionPrototype(): GameSessionPrototype = _gameSessionPrototype ?: GameSessionPrototype(
        ship = shipPrototype,
        engine = getEngines().random(),
        formula = Formula()
    ).also { _gameSessionPrototype = it }
}
