package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
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

// Type aliases for the generated schema classes to provide more convenient names
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
