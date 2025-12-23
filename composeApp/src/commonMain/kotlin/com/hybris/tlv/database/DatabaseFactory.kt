package com.hybris.tlv.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.adapter.SetColumnAdapter
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import database.Achievement
import database.AppDatabase
import database.Catastrophe
import database.Credit
import database.Engine
import database.Event
import database.Formula
import database.GameSession
import database.Planet
import database.Ship
import database.StellarHost
import database.Translation

/**
 * Factory for creating and configuring the [AppDatabase] instance with the necessary column adapters for custom data types, given a [SqlDriver].
 */
internal class DatabaseFactory(private val driver: SqlDriver) {

    private val shipAdapter = Ship.Adapter(
        assignedPointsAdapter = IntColumnAdapter,
        sensorRangeAdapter = IntColumnAdapter,
        integrityAdapter = IntColumnAdapter,
        fuelAdapter = IntColumnAdapter,
        materialsAdapter = IntColumnAdapter,
        cryopodsAdapter = IntColumnAdapter
    )

    private val engineAdapter = Engine.Adapter(
        costAdapter = IntColumnAdapter
    )

    private val planetAdapter = Planet.Adapter(
        statusAdapter = EnumColumnAdapter()
    )

    private val gameSessionAdapter = GameSession.Adapter(
        visitedStellarHostsAdapter = SetColumnAdapter,
        launchedEventsAdapter = SetColumnAdapter
    )

    private val creditAdapter = Credit.Adapter(
        typeAdapter = EnumColumnAdapter()
    )

    /**
     * The configured [AppDatabase] instance.
     */
    val database: AppDatabase = AppDatabase(
        driver = driver,
        ShipAdapter = shipAdapter,
        EngineAdapter = engineAdapter,
        PlanetAdapter = planetAdapter,
        GameSessionAdapter = gameSessionAdapter,
        CreditAdapter = creditAdapter
    )
}

internal const val DATABASE_FILE = "tlv_database.db"

// Type aliases for the generated schema classes to provide more convenient names.
typealias TranslationSchema = Translation
typealias StellarHostSchema = StellarHost
typealias PlanetSchema = Planet
typealias FormulaSchema = Formula
typealias CatastropheSchema = Catastrophe
typealias EngineSchema = Engine
typealias ShipSchema = Ship
typealias GameSessionSchema = GameSession
typealias EventSchema = Event
typealias AchievementSchema = Achievement
typealias CreditSchema = Credit

/**
 * Clears all tables in the database.
 */
internal suspend fun AppDatabase.reset() = withContext(context = Dispatcher.IO) {
    runCatching {
        transaction {
            translationQueries.truncateTranslation()
            stellarHostQueries.truncateStellarHost()
            planetQueries.truncatePlanet()
            formulaQueries.truncateFormula()
            catastropheQueries.truncateCatastrophe()
            engineQueries.truncateEngine()
            shipQueries.truncateShip()
            gameSessionQueries.truncateGameSession()
            eventQueries.truncateEvent()
            achievementQueries.truncateAchievement()
            creditQueries.truncateCredit()
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to clear database", throwable = it) }.getOrDefault(defaultValue = Unit)
}

/**
 * Checks if the database is empty.
 */
internal suspend fun AppDatabase.isEmpty(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        transactionWithResult {
            translationQueries.isTranslationEmpty().executeAsList().isEmpty() &&
                    stellarHostQueries.isStellarHostEmpty().executeAsList().isEmpty() &&
                    planetQueries.isPlanetEmpty().executeAsList().isEmpty() &&
                    formulaQueries.isFormulaEmpty().executeAsList().isEmpty() &&
                    catastropheQueries.isCatastropheEmpty().executeAsList().isEmpty() &&
                    engineQueries.isEngineEmpty().executeAsList().isEmpty() &&
                    shipQueries.isShipEmpty().executeAsList().isEmpty() &&
                    gameSessionQueries.isGameSessionEmpty().executeAsList().isEmpty() &&
                    eventQueries.isEventEmpty().executeAsList().isEmpty() &&
                    achievementQueries.isAchievementEmpty().executeAsList().isEmpty() &&
                    creditQueries.isCreditEmpty().executeAsList().isEmpty()
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to clear database", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Database"
