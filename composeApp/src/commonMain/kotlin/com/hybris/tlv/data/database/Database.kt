package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
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
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to clear database", throwable = it)
    }.getOrDefault(defaultValue = Unit)
}

private const val TAG = "Database"
