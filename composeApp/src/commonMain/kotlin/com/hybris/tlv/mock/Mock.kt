package com.hybris.tlv.mock

import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.Database
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.CommonHttpClientFactory
import com.hybris.tlv.http.client.ExoPlanetClient
import com.hybris.tlv.http.client.HttpClient
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.CommonLocalConfig
import com.hybris.tlv.storage.CommonRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
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
import com.hybris.tlv.usecase.credits.CreditsInternalGateway
import com.hybris.tlv.usecase.credits.CreditsInternalUseCases
import com.hybris.tlv.usecase.credits.local.CreditsDao
import com.hybris.tlv.usecase.credits.local.CreditsLocal
import com.hybris.tlv.usecase.credits.remote.CreditsApi
import com.hybris.tlv.usecase.credits.remote.CreditsRemote
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
import io.ktor.client.HttpClient as KtorHttpClient

internal class Mock(
    val dispatcher: Dispatcher = CommonDispatchers(),
    val locale: Locale = CommonLocale(),
    val localConfig: LocalConfig = CommonLocalConfig(),
    val remoteConfig: RemoteConfig = CommonRemoteConfig(),
    val firestore: Firestore = CommonFirestore(),
    val sqlDriver: SqlDriver = createSqlDriver(inMemory = true),
    val httpClientFactory: HttpClientFactory = CommonHttpClientFactory(),
    val database: AppDatabase = Database(driver = sqlDriver).database,
    val httpClient: KtorHttpClient = httpClientFactory.buildExoplanetHttpClient(),
    val exoplanetHttpClient: HttpClient = ExoPlanetClient(httpClient = httpClient),
    val translationDao: TranslationLocal = TranslationDao(database = database),
    val earthDao: EarthLocal = EarthDao(database = database),
    val shipDao: ShipLocal = ShipDao(database = database),
    val spaceDao: SpaceLocal = SpaceDao(database = database),
    val eventDao: EventLocal = EventDao(database = database),
    val gameSessionDao: GameSessionLocal = GameSessionDao(database = database),
    val achievementDao: AchievementLocal = AchievementDao(database = database),
    val creditsDao: CreditsLocal = CreditsDao(database = database),
    val translationApi: TranslationRemote = TranslationApi(firestore = firestore),
    val earthApi: EarthRemote = EarthApi(firestore = firestore),
    val shipApi: ShipRemote = ShipApi(firestore = firestore),
    val spaceApi: SpaceRemote = SpaceApi(
        exoplanetHttpClient = exoplanetHttpClient,
        firestore = firestore
    ),
    val eventApi: EventRemote = EventApi(firestore = firestore),
    val achievementApi: AchievementRemote = AchievementApi(firestore = firestore),
    val creditsApi: CreditsRemote = CreditsApi(firestore = firestore),
    val internalTranslation: TranslationInternalUseCases = TranslationInternalGateway(
        dispatcher = dispatcher,
        translationApi = translationApi,
        translationDao = translationDao
    ),
    val internalEarth: EarthInternalUseCases = EarthInternalGateway(
        earthApi = earthApi,
        earthDao = earthDao
    ),
    val internalShip: ShipInternalUseCases = ShipInternalGateway(
        shipApi = shipApi,
        shipDao = shipDao
    ),
    val internalSpace: SpaceInternalUseCases = SpaceInternalGateway(
        spaceApi = spaceApi,
        spaceDao = spaceDao
    ),
    val internalEvent: EventInternalUseCases = EventInternalGateway(
        eventApi = eventApi,
        eventDao = eventDao
    ),
    val internalAchievement: AchievementInternalUseCases = AchievementInternalGateway(
        achievementApi = achievementApi,
        achievementDao = achievementDao
    ),
    val internalCredits: CreditsInternalUseCases = CreditsInternalGateway(
        creditsApi = creditsApi,
        creditsDao = creditsDao
    ),
) {
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        firestore = firestore,
        sqlDriver = sqlDriver,
        httpClientFactory = httpClientFactory,
        database = database,
        httpClient = httpClient,
        exoplanetHttpClient = exoplanetHttpClient,
        translationDao = translationDao,
        earthDao = earthDao,
        shipDao = shipDao,
        spaceDao = spaceDao,
        eventDao = eventDao,
        gameSessionDao = gameSessionDao,
        achievementDao = achievementDao,
        creditsDao = creditsDao,
        translationApi = translationApi,
        earthApi = earthApi,
        shipApi = shipApi,
        spaceApi = spaceApi,
        eventApi = eventApi,
        achievementApi = achievementApi,
        creditsApi = creditsApi,
        internalTranslation = internalTranslation,
        internalEarth = internalEarth,
        internalShip = internalShip,
        internalSpace = internalSpace,
        internalEvent = internalEvent,
        internalAchievement = internalAchievement,
        internalCredits = internalCredits,
    )
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )
}
