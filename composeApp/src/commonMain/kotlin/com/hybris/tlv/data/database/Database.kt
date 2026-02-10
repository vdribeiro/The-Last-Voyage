package com.hybris.tlv.data.database

import kotlinx.coroutines.withContext
import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
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
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to clear database", throwable = it) }.getOrDefault(defaultValue = Unit)
}

internal object NoOpSqlDriver: SqlDriver {
    override fun <R> executeQuery(
        identifier: Int?,
        sql: String,
        mapper: (SqlCursor) -> QueryResult<R>,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ): QueryResult<R> = mapper(NoOpCursor)

    override fun execute(identifier: Int?, sql: String, parameters: Int, binders: (SqlPreparedStatement.() -> Unit)?): QueryResult<Long> = QueryResult.Value(value = 0L)
    override fun newTransaction(): QueryResult<Transacter.Transaction> = QueryResult.Value(value = NoOpTransaction)
    override fun currentTransaction(): Transacter.Transaction? = null
    override fun addListener(vararg queryKeys: String, listener: Query.Listener) {}
    override fun removeListener(vararg queryKeys: String, listener: Query.Listener) {}
    override fun notifyListeners(vararg queryKeys: String) {}
    override fun close() {}

    private object NoOpCursor: SqlCursor {
        override fun next(): QueryResult<Boolean> = QueryResult.Value(value = false)
        override fun getString(index: Int): String? = null
        override fun getLong(index: Int): Long? = null
        override fun getBytes(index: Int): ByteArray? = null
        override fun getDouble(index: Int): Double? = null
        override fun getBoolean(index: Int): Boolean? = null
    }

    private object NoOpTransaction: Transacter.Transaction() {
        override val enclosingTransaction: Transacter.Transaction? = null
        override fun endTransaction(successful: Boolean): QueryResult<Unit> = QueryResult.Value(value = Unit)
    }
}

private const val TAG = "Database"
