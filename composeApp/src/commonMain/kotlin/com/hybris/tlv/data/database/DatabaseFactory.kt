package com.hybris.tlv.data.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.data.database.adapter.SetColumnAdapter
import database.AppDatabase
import database.Credit
import database.Engine
import database.GameSession
import database.Planet
import database.Ship

/**
 * Factory responsible for initializing the [AppDatabase] with standardized column adapters.
 *
 * The [SqlDriver] is supplied by the specific platform, while this factory ensures that custom types—such as
 * Enums, Sets, and Primitive wrappers—are mapped correctly to SQL types across all targets.
 *
 * @param driver The platform-specific [SqlDriver] used to establish the database connection.
 */
internal class DatabaseFactory(driver: SqlDriver) {

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
     * The fully configured [AppDatabase] instance.
     */
    val database: AppDatabase = AppDatabase(
        driver = driver,
        ShipAdapter = shipAdapter,
        EngineAdapter = engineAdapter,
        PlanetAdapter = planetAdapter,
        GameSessionAdapter = gameSessionAdapter,
        CreditAdapter = creditAdapter
    )

    companion object {
        const val DATABASE_FILE = "tlv_database.db"
    }
}
