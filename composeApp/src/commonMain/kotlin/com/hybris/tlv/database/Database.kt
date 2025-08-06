package com.hybris.tlv.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.adapter.SetColumnAdapter
import database.Achievement
import database.AppDatabase
import database.Catastrophe
import database.Credits
import database.Engine
import database.Event
import database.GameSession
import database.Planet
import database.StellarHost
import database.Translation

internal class Database(val driver: SqlDriver) {

    private val creditsAdapter by lazy {
        Credits.Adapter(
            typeAdapter = EnumColumnAdapter()
        )
    }

    private val gameSessionAdapter by lazy {
        GameSession.Adapter(
            assignedPointsAdapter = IntColumnAdapter,
            sensorRangeAdapter = IntColumnAdapter,
            integrityAdapter = IntColumnAdapter,
            fuelAdapter = IntColumnAdapter,
            materialsAdapter = IntColumnAdapter,
            cryopodsAdapter = IntColumnAdapter,
            visitedStellarHostsAdapter = SetColumnAdapter,
            launchedEventsAdapter = SetColumnAdapter
        )
    }

    private val planetAdapter by lazy {
        Planet.Adapter(
            statusAdapter = EnumColumnAdapter()
        )
    }

    val database: AppDatabase by lazy {
        AppDatabase(
            driver = driver,
            CreditsAdapter = creditsAdapter,
            GameSessionAdapter = gameSessionAdapter,
            PlanetAdapter = planetAdapter,
        )
    }

    companion object {
        const val NAME = "TLVDatabase.db"
    }
}

typealias TranslationSchema = Translation
typealias StellarHostSchema = StellarHost
typealias PlanetSchema = Planet
typealias CatastropheSchema = Catastrophe
typealias EngineSchema = Engine
typealias GameSessionSchema = GameSession
typealias EventSchema = Event
typealias AchievementSchema = Achievement
typealias CreditsSchema = Credits
