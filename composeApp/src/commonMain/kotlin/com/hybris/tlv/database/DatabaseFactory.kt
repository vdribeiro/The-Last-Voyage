package com.hybris.tlv.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.adapter.SetColumnAdapter
import database.Achievement
import database.AppDatabase
import database.Catastrophe
import database.Credit
import database.Engine
import database.Event
import database.Formula
import database.GameSession
import database.Learning
import database.Planet
import database.Ship
import database.StellarHost
import database.Translation

internal class DatabaseFactory(private val driver: SqlDriver) {

    private val learningAdapter = Learning.Adapter(
        typeAdapter = EnumColumnAdapter()
    )

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

    val database: AppDatabase = AppDatabase(
        driver = driver,
        LearningAdapter = learningAdapter,
        ShipAdapter = shipAdapter,
        EngineAdapter = engineAdapter,
        PlanetAdapter = planetAdapter,
        GameSessionAdapter = gameSessionAdapter,
        CreditAdapter = creditAdapter,
    )
}

internal const val DATABASE_FILE = "tlv_app.db"

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
typealias LearningSchema = Learning
