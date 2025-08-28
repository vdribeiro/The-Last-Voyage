package com.hybris.tlv

import androidx.annotation.VisibleForTesting
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.Config
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.Database
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.achievement.AchievementInternalGateway
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.achievement.local.AchievementDao
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.remote.AchievementApi
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote
import com.hybris.tlv.usecase.credit.CreditInternalGateway
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.credit.local.CreditDao
import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.credit.remote.CreditApi
import com.hybris.tlv.usecase.credit.remote.CreditRemote
import com.hybris.tlv.usecase.earth.EarthInternalGateway
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.earth.local.EarthDao
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.remote.EarthApi
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import com.hybris.tlv.usecase.event.EventInternalGateway
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.event.local.EventDao
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.remote.EventApi
import com.hybris.tlv.usecase.event.remote.EventRemote
import com.hybris.tlv.usecase.gamesession.local.GameSessionDao
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.ship.ShipInternalGateway
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.ship.local.ShipDao
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.remote.ShipApi
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import com.hybris.tlv.usecase.space.SpaceInternalGateway
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.space.local.SpaceDao
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.remote.SpaceApi
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.translation.TranslationInternalGateway
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases
import com.hybris.tlv.usecase.translation.local.TranslationDao
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.remote.TranslationApi
import com.hybris.tlv.usecase.translation.remote.TranslationRemote
import database.AppDatabase
import io.ktor.client.HttpClient

internal class Core(
    @get:VisibleForTesting internal val dispatcher: Dispatcher = Dispatchers(),
    @get:VisibleForTesting internal val sqlDriver: SqlDriver = createSqlDriver(),
    @get:VisibleForTesting internal val database: AppDatabase = Database(driver = sqlDriver).database,
    @get:VisibleForTesting internal val httpClient: HttpClient = HttpClientFactory.buildHttpClient(),
    @get:VisibleForTesting internal val translationDao: TranslationLocal = TranslationDao(database = database),
    @get:VisibleForTesting internal val earthDao: EarthLocal = EarthDao(database = database),
    @get:VisibleForTesting internal val shipDao: ShipLocal = ShipDao(database = database),
    @get:VisibleForTesting internal val spaceDao: SpaceLocal = SpaceDao(database = database),
    @get:VisibleForTesting internal val eventDao: EventLocal = EventDao(database = database),
    @get:VisibleForTesting internal val gameSessionDao: GameSessionLocal = GameSessionDao(database = database),
    @get:VisibleForTesting internal val achievementDao: AchievementLocal = AchievementDao(database = database),
    @get:VisibleForTesting internal val creditDao: CreditLocal = CreditDao(database = database),
    @get:VisibleForTesting internal val translationApi: TranslationRemote = TranslationApi(httpClient = httpClient),
    @get:VisibleForTesting internal val earthApi: EarthRemote = EarthApi(httpClient = httpClient),
    @get:VisibleForTesting internal val shipApi: ShipRemote = ShipApi(httpClient = httpClient),
    @get:VisibleForTesting internal val spaceApi: SpaceRemote = SpaceApi(httpClient = httpClient),
    @get:VisibleForTesting internal val eventApi: EventRemote = EventApi(httpClient = httpClient),
    @get:VisibleForTesting internal val achievementApi: AchievementRemote = AchievementApi(httpClient = httpClient),
    @get:VisibleForTesting internal val creditApi: CreditRemote = CreditApi(httpClient = httpClient),
    @get:VisibleForTesting internal val internalTranslation: TranslationInternalUseCases = TranslationInternalGateway(
        dispatcher = dispatcher,
        translationApi = translationApi,
        translationDao = translationDao
    ),
    @get:VisibleForTesting internal val internalEarth: EarthInternalUseCases = EarthInternalGateway(
        earthApi = earthApi,
        earthDao = earthDao
    ),
    @get:VisibleForTesting internal val internalShip: ShipInternalUseCases = ShipInternalGateway(
        shipApi = shipApi,
        shipDao = shipDao
    ),
    @get:VisibleForTesting internal val internalSpace: SpaceInternalUseCases = SpaceInternalGateway(
        spaceApi = spaceApi,
        spaceDao = spaceDao
    ),
    @get:VisibleForTesting internal val internalEvent: EventInternalUseCases = EventInternalGateway(
        eventApi = eventApi,
        eventDao = eventDao
    ),
    @get:VisibleForTesting internal val internalAchievement: AchievementInternalUseCases = AchievementInternalGateway(
        achievementApi = achievementApi,
        achievementDao = achievementDao
    ),
    @get:VisibleForTesting internal val internalCredit: CreditInternalUseCases = CreditInternalGateway(
        creditApi = creditApi,
        creditDao = creditDao
    ),
    @get:VisibleForTesting internal val config: ConfigManager = Config(httpClient = httpClient),
    @get:VisibleForTesting internal val useCases: UseCases = Gateways(
        config = config,
        earthDao = earthDao,
        shipDao = shipDao,
        spaceDao = spaceDao,
        eventDao = eventDao,
        gameSessionDao = gameSessionDao,
        achievementDao = achievementDao,
        creditDao = creditDao,
        internalTranslation = internalTranslation,
        internalEarth = internalEarth,
        internalShip = internalShip,
        internalSpace = internalSpace,
        internalEvent = internalEvent,
        internalAchievement = internalAchievement,
        internalCredit = internalCredit,
    ),
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        config = config,
        useCases = useCases
    )
)
