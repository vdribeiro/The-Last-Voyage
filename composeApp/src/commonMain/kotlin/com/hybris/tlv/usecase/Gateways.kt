package com.hybris.tlv.usecase

import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.Database
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.CommonLocalConfig
import com.hybris.tlv.storage.CommonRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.usecase.achievement.AchievementGateway
import com.hybris.tlv.usecase.achievement.AchievementInternalGateway
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.local.AchievementDao
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.remote.AchievementApi
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote
import com.hybris.tlv.usecase.credit.CreditGateway
import com.hybris.tlv.usecase.credit.CreditInternalGateway
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.credit.local.CreditDao
import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.credit.remote.CreditApi
import com.hybris.tlv.usecase.credit.remote.CreditRemote
import com.hybris.tlv.usecase.earth.EarthGateway
import com.hybris.tlv.usecase.earth.EarthInternalGateway
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.earth.local.EarthDao
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.remote.EarthApi
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import com.hybris.tlv.usecase.event.EventGateway
import com.hybris.tlv.usecase.event.EventInternalGateway
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.local.EventDao
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.remote.EventApi
import com.hybris.tlv.usecase.event.remote.EventRemote
import com.hybris.tlv.usecase.gamesession.GameSessionGateway
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.local.GameSessionDao
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.ship.ShipGateway
import com.hybris.tlv.usecase.ship.ShipInternalGateway
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.local.ShipDao
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.remote.ShipApi
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceInternalGateway
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.local.SpaceDao
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.remote.SpaceApi
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.sync.SyncGateway
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.translation.TranslationInternalGateway
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases
import com.hybris.tlv.usecase.translation.local.TranslationDao
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.remote.TranslationApi
import com.hybris.tlv.usecase.translation.remote.TranslationRemote
import database.AppDatabase
import io.ktor.client.HttpClient

internal class Gateways(
    dispatcher: Dispatcher = CommonDispatchers(),
    locale: Locale = CommonLocale(),
    localConfig: LocalConfig = CommonLocalConfig(),
    remoteConfig: RemoteConfig = CommonRemoteConfig(),
    sqlDriver: SqlDriver = createSqlDriver(inMemory = true),
    database: AppDatabase = Database(driver = sqlDriver).database,
    httpClient: HttpClient = HttpClientFactory.buildHttpClient(),
    translationDao: TranslationLocal = TranslationDao(database = database),
    earthDao: EarthLocal = EarthDao(database = database),
    shipDao: ShipLocal = ShipDao(database = database),
    spaceDao: SpaceLocal = SpaceDao(database = database),
    eventDao: EventLocal = EventDao(database = database),
    gameSessionDao: GameSessionLocal = GameSessionDao(database = database),
    achievementDao: AchievementLocal = AchievementDao(database = database),
    creditDao: CreditLocal = CreditDao(database = database),
    translationApi: TranslationRemote = TranslationApi(firestore = firestore),
    earthApi: EarthRemote = EarthApi(firestore = firestore),
    shipApi: ShipRemote = ShipApi(firestore = firestore),
    spaceApi: SpaceRemote = SpaceApi(
        httpClient = httpClient,
        firestore = firestore
    ),
    eventApi: EventRemote = EventApi(firestore = firestore),
    achievementApi: AchievementRemote = AchievementApi(firestore = firestore),
    creditApi: CreditRemote = CreditApi(firestore = firestore),
    internalTranslation: TranslationInternalUseCases = TranslationInternalGateway(
        dispatcher = dispatcher,
        translationApi = translationApi,
        translationDao = translationDao
    ),
    internalEarth: EarthInternalUseCases = EarthInternalGateway(
        earthApi = earthApi,
        earthDao = earthDao
    ),
    internalShip: ShipInternalUseCases = ShipInternalGateway(
        shipApi = shipApi,
        shipDao = shipDao
    ),
    internalSpace: SpaceInternalUseCases = SpaceInternalGateway(
        spaceApi = spaceApi,
        spaceDao = spaceDao
    ),
    internalEvent: EventInternalUseCases = EventInternalGateway(
        eventApi = eventApi,
        eventDao = eventDao
    ),
    internalAchievement: AchievementInternalUseCases = AchievementInternalGateway(
        achievementApi = achievementApi,
        achievementDao = achievementDao
    ),
    internalCredit: CreditInternalUseCases = CreditInternalGateway(
        creditApi = creditApi,
        creditDao = creditDao
    ),
): UseCases {

    override val earth: EarthUseCases by lazy {
        EarthGateway(
            earthDao = earthDao
        )
    }

    override val ship: ShipUseCases by lazy {
        ShipGateway(
            shipDao = shipDao
        )
    }

    override val space: SpaceUseCases by lazy {
        SpaceGateway(
            spaceDao = spaceDao,
        )
    }

    override val event: EventUseCases by lazy {
        EventGateway(
            eventDao = eventDao
        )
    }

    override val gameSession: GameSessionUseCases by lazy {
        GameSessionGateway(
            gameSessionDao = gameSessionDao
        )
    }

    override val achievement: AchievementUseCases by lazy {
        AchievementGateway(
            achievementDao = achievementDao
        )
    }

    override val credit: CreditUseCases by lazy {
        CreditGateway(
            creditDao = creditDao
        )
    }

    override val sync: SyncUseCases by lazy {
        SyncGateway(
            locale = locale,
            localConfig = localConfig,
            remoteConfig = remoteConfig,
            internalTranslation = internalTranslation,
            internalEarth = internalEarth,
            internalShip = internalShip,
            internalSpace = internalSpace,
            internalEvent = internalEvent,
            internalAchievement = internalAchievement,
            internalCredit = internalCredit
        )
    }
}
