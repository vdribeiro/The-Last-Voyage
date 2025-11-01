package com.hybris.tlv

import kotlinx.coroutines.runBlocking
import com.hybris.tlv.config.Configs
import com.hybris.tlv.config.Preferences
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.locale.now
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.serializer.ACHIEVEMENTS_JSON
import com.hybris.tlv.serializer.CATASTROPHES_JSON
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.CREDITS_JSON
import com.hybris.tlv.serializer.ENGINES_JSON
import com.hybris.tlv.serializer.EVENTS_JSON
import com.hybris.tlv.serializer.LEARNINGS_JSON
import com.hybris.tlv.serializer.SOLAR_HOSTS_JSON
import com.hybris.tlv.serializer.SOLAR_PLANETS_JSON
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.model.Translation

internal val testDependency: Dependency by lazy {
    Dependency(
        dispatcher = TestDispatchers(),
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines.testEngine,
        audioPlayer = AudioPlayer(),
    )
}

internal fun reset() {
    runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.setPreferences { Preferences() }
        testDependency.config.setConfigs { Configs() }
    }
}

internal val storeFactory: StoreFactory by lazy {
    StoreFactory(
        dispatcher = testDependency.dispatcher,
        navigation = testDependency.navigation,
        audioPlayer = testDependency.audioPlayer,
        config = testDependency.config,
        useCases = testDependency.useCases
    )
}

internal val configs: List<Configs> by lazy {
    runBlocking { loadFromJsonResource(path = CONFIGS_JSON) }
}
internal val translations: List<Translation> by lazy {
    runBlocking { loadFromJsonResource(path = TRANSLATIONS_JSON) }
}
internal val learnings: List<Learning> by lazy {
    runBlocking { loadFromJsonResource(path = LEARNINGS_JSON) }
}
internal val catastrophes: List<Catastrophe> by lazy {
    runBlocking { loadFromJsonResource(path = CATASTROPHES_JSON) }
}
internal val engines: List<Engine> by lazy {
    runBlocking { loadFromJsonResource(path = ENGINES_JSON) }
}
internal val events: List<Event> by lazy {
    runBlocking { loadFromJsonResource(path = EVENTS_JSON) }
}
internal val achievements: List<Achievement> by lazy {
    runBlocking { loadFromJsonResource(path = ACHIEVEMENTS_JSON) }
}
internal val credits: List<Credit> by lazy {
    runBlocking { loadFromJsonResource(path = CREDITS_JSON) }
}
internal val stellarHosts: List<StellarHost> by lazy {
    runBlocking { loadFromJsonResource(path = SOLAR_HOSTS_JSON) }
}
internal val planets: List<Planet> by lazy {
    runBlocking { loadFromJsonResource(path = SOLAR_PLANETS_JSON) }
}
internal val hostsWithPlanets: List<StellarHost> by lazy {
    val planetsMap = planets.groupBy { it.stellarHostId }
    stellarHosts.apply {
        forEach { it.planets.addAll(elements = planetsMap[it.id].orEmpty()) }
    }.sortedWith(comparator = compareBy<StellarHost, Double?>(comparator = nullsLast()) { it.distance }.thenBy { it.id })
}
internal val formula: Formula by lazy {
    Formula(id = "1")
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
        formula = formula
    )
}
internal val gameSession: GameSession by lazy {
    GameSession(
        id = "1",
        utc = now(),
        currentStellarHostId = stellarHosts.random().id,
        visitedStellarHosts = emptySet(),
        launchedEvents = emptySet(),
        settledPlanetId = null,
        settledPlanetName = null,
        finalHabitability = null,
        score = null,
        ship = ship,
        formula = formula
    )
}
