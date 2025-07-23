package com.hybris.tlv.usecase

import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.Database
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.ExoPlanetClient
import com.hybris.tlv.usecase.achievement.AchievementGateway
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.local.AchievementDao
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.remote.AchievementApi
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote
import com.hybris.tlv.usecase.credits.CreditsGateway
import com.hybris.tlv.usecase.credits.CreditsUseCases
import com.hybris.tlv.usecase.credits.local.CreditsDao
import com.hybris.tlv.usecase.credits.local.CreditsLocal
import com.hybris.tlv.usecase.credits.remote.CreditsApi
import com.hybris.tlv.usecase.credits.remote.CreditsRemote
import com.hybris.tlv.usecase.earth.EarthGateway
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.earth.local.EarthDao
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.remote.EarthApi
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import com.hybris.tlv.usecase.event.EventGateway
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.local.EventDao
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.remote.EventApi
import com.hybris.tlv.usecase.event.remote.EventRemote
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway
import com.hybris.tlv.usecase.exoplanet.ExoplanetUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionGateway
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.local.GameSessionDao
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.ship.ShipGateway
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.local.ShipDao
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.remote.ShipApi
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.local.SpaceDao
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.remote.SpaceApi
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.translation.TranslationGateway
import com.hybris.tlv.usecase.translation.TranslationUseCases
import com.hybris.tlv.usecase.translation.local.TranslationDao
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.remote.TranslationApi
import com.hybris.tlv.usecase.translation.remote.TranslationRemote
import io.ktor.client.HttpClient

internal class Gateways(
    dispatcher: Dispatcher,
    firestore: Firestore,
    databaseDriver: SqlDriver,
    httpClient: HttpClient,
): UseCases {

    private val database by lazy {
        Database(driver = databaseDriver).database
    }

    private val exoplanetHttpClient by lazy {
        ExoPlanetClient(httpClient = httpClient)
    }

    private val translationDao: TranslationLocal by lazy {
        TranslationDao(database = database)
    }

    private val earthDao: EarthLocal by lazy {
        EarthDao(database = database)
    }

    private val shipDao: ShipLocal by lazy {
        ShipDao(database = database)
    }

    private val spaceDao: SpaceLocal by lazy {
        SpaceDao(database = database)
    }

    private val eventDao: EventLocal by lazy {
        EventDao(database = database)
    }

    private val gameSessionDao: GameSessionLocal by lazy {
        GameSessionDao(database = database)
    }

    private val achievementDao: AchievementLocal by lazy {
        AchievementDao(database = database)
    }

    private val creditsDao: CreditsLocal by lazy {
        CreditsDao(database = database)
    }

    private val translationApi: TranslationRemote by lazy {
        TranslationApi(firestore = firestore)
    }

    private val earthApi: EarthRemote by lazy {
        EarthApi(firestore = firestore)
    }

    private val shipApi: ShipRemote by lazy {
        ShipApi(firestore = firestore)
    }

    private val spaceApi: SpaceRemote by lazy {
        SpaceApi(
            exoplanetHttpClient = exoplanetHttpClient,
            firestore = firestore
        )
    }

    private val eventApi: EventRemote by lazy {
        EventApi(firestore = firestore)
    }

    private val achievementApi: AchievementRemote by lazy {
        AchievementApi(firestore = firestore)
    }

    private val creditsApi: CreditsRemote by lazy {
        CreditsApi(firestore = firestore)
    }

    override val translation: TranslationUseCases by lazy {
        TranslationGateway(
            dispatcher = dispatcher,
            translationApi = translationApi,
            translationDao = translationDao
        )
    }

    override val earth: EarthUseCases by lazy {
        EarthGateway(
            earthApi = earthApi,
            earthDao = earthDao
        )
    }

    override val ship: ShipUseCases by lazy {
        ShipGateway(
            shipApi = shipApi,
            shipDao = shipDao
        )
    }

    override val space: SpaceUseCases by lazy {
        SpaceGateway(
            spaceApi = spaceApi,
            spaceDao = spaceDao,
        )
    }

    override val event: EventUseCases by lazy {
        EventGateway(
            eventApi = eventApi,
            eventDao = eventDao
        )
    }

    override val exoplanet: ExoplanetUseCases by lazy {
        ExoplanetGateway()
    }

    override val gameSession: GameSessionUseCases by lazy {
        GameSessionGateway(
            gameSessionDao = gameSessionDao
        )
    }

    override val achievement: AchievementUseCases by lazy {
        AchievementGateway(
            achievementApi = achievementApi,
            achievementDao = achievementDao
        )
    }

    override val credits: CreditsUseCases by lazy {
        CreditsGateway(
            creditsApi = creditsApi,
            creditsDao = creditsDao
        )
    }
}
