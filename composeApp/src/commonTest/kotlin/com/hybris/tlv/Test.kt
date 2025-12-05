package com.hybris.tlv

import kotlinx.coroutines.runBlocking
import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.http.TestEngines
import com.hybris.tlv.locale.now
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.serializer.ACHIEVEMENTS_JSON
import com.hybris.tlv.serializer.CATASTROPHES_JSON
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.CREDITS_JSON
import com.hybris.tlv.serializer.ENGINES_JSON
import com.hybris.tlv.serializer.EVENTS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.serializer.SOLAR_HOSTS_JSON
import com.hybris.tlv.serializer.SOLAR_PLANETS_JSON
import com.hybris.tlv.serializer.TRANSLATIONS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.storage.deleteFile
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.cheat.CheatStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.model.Translation

private val dependency: Dependency by lazy {
    Dependency(
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngines.testEngine,
        audioPlayer = AudioPlayer(),
    )
}

internal fun reset() {
    runBlocking {
        deleteFile(path = CONFIGS_JSON)
        deleteFile(path = PREFERENCES_JSON)
        dependency.sqlDriver.clearDatabase()
    }
}

internal val useCases: UseCases = dependency.useCases

internal fun getSplashStore(): SplashStore =
    SplashStore(
        config = dependency.config,
        archiveUseCases = useCases.archive,
        translateUseCases = useCases.translation,
        catastropheUseCases = useCases.catastrophe,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        eventUseCases = useCases.event,
        achievementUseCases = useCases.achievement,
        creditUseCases = useCases.credit
    )

internal fun getCheatStore(): CheatStore =
    CheatStore(config = dependency.config)

internal fun getMainMenuStore(): MainMenuStore =
    MainMenuStore(
        config = dependency.config,
        gameSessionUseCases = useCases.gameSession,
    )

internal fun getHelpStore(): HelpStore =
    HelpStore(config = dependency.config)

internal fun getFeedbackStore(tag: String? = null, message: String? = null) =
    FeedbackStore(
        tag = tag,
        message = message,
    )

internal fun getNewGameStore(): NewGameStore =
    NewGameStore(
        shipUseCases = useCases.ship,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

internal fun getTutorialStore(newGame: Boolean = false): TutorialStore =
    TutorialStore(
        newGame = newGame,
        config = dependency.config
    )

internal fun getGameStore(ship: Ship? = com.hybris.tlv.ship): GameStore =
    GameStore(
        ship = ship,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        gameSessionUseCases = useCases.gameSession
    )

internal fun getEventStore(ship: Ship? = com.hybris.tlv.ship): EventStore =
    EventStore(
        ship = ship,
        eventUseCases = useCases.event,
        gameSessionUseCases = useCases.gameSession,
    )

internal fun getGameOverStore(): GameOverStore =
    GameOverStore(
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

internal fun getStellarExplorerStore(): StellarExplorerStore =
    StellarExplorerStore(spaceUseCases = useCases.space)

internal fun getScoreStore(): ScoreStore =
    ScoreStore(gameSessionUseCases = useCases.gameSession)

internal fun getAchievementStore(): AchievementStore =
    AchievementStore(achievementUseCases = useCases.achievement)

internal fun getCreditStore(): CreditStore =
    CreditStore(creditUseCases = useCases.credit)

internal val configs: List<Configs> by lazy {
    runBlocking { loadFromJsonResource(path = CONFIGS_JSON) }
}
internal val translations: List<Translation> by lazy {
    runBlocking { loadFromJsonResource(path = TRANSLATIONS_JSON) }
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
