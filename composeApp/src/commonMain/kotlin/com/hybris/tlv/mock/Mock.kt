package com.hybris.tlv.mock

import app.cash.sqldelight.db.QueryResult
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

internal class Mock(
    val dispatcher: Dispatcher = CommonDispatchers(),
    val locale: Locale = CommonLocale(),
    val localConfig: LocalConfig = CommonLocalConfig(),
    val remoteConfig: RemoteConfig = CommonRemoteConfig(),
    val sqlDriver: SqlDriver = createSqlDriver(inMemory = true),
    val database: AppDatabase = Database(driver = sqlDriver).database,
    val httpClient: HttpClient = HttpClientFactory.buildHttpClient(),
    val translationDao: TranslationLocal = TranslationDao(database = database),
    val earthDao: EarthLocal = EarthDao(database = database),
    val shipDao: ShipLocal = ShipDao(database = database),
    val spaceDao: SpaceLocal = SpaceDao(database = database),
    val eventDao: EventLocal = EventDao(database = database),
    val gameSessionDao: GameSessionLocal = GameSessionDao(database = database),
    val achievementDao: AchievementLocal = AchievementDao(database = database),
    val creditDao: CreditLocal = CreditDao(database = database),
    val translationApi: TranslationRemote = TranslationApi(httpClient = httpClient),
    val earthApi: EarthRemote = EarthApi(httpClient = httpClient),
    val shipApi: ShipRemote = ShipApi(httpClient = httpClient),
    val spaceApi: SpaceRemote = SpaceApi(httpClient = httpClient),
    val eventApi: EventRemote = EventApi(httpClient = httpClient),
    val achievementApi: AchievementRemote = AchievementApi(httpClient = httpClient),
    val creditApi: CreditRemote = CreditApi(httpClient = httpClient),
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
    val internalCredit: CreditInternalUseCases = CreditInternalGateway(
        creditApi = creditApi,
        creditDao = creditDao
    ),
) {
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        sqlDriver = sqlDriver,
        database = database,
        httpClient = httpClient,
        translationDao = translationDao,
        earthDao = earthDao,
        shipDao = shipDao,
        spaceDao = spaceDao,
        eventDao = eventDao,
        gameSessionDao = gameSessionDao,
        achievementDao = achievementDao,
        creditDao = creditDao,
        translationApi = translationApi,
        earthApi = earthApi,
        shipApi = shipApi,
        spaceApi = spaceApi,
        eventApi = eventApi,
        achievementApi = achievementApi,
        creditApi = creditApi,
        internalTranslation = internalTranslation,
        internalEarth = internalEarth,
        internalShip = internalShip,
        internalSpace = internalSpace,
        internalEvent = internalEvent,
        internalAchievement = internalAchievement,
        internalCredit = internalCredit,
    )
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )

    fun clearDatabase() {
        val query = "SELECT name FROM sqlite_master WHERE type='table' " +
                "AND name!='sqlite_sequence' AND name!='android_metadata'"
        sqlDriver.executeQuery(
            identifier = null,
            sql = query,
            mapper = { cursor ->
                QueryResult.Value(value = buildList {
                    while (cursor.next().value) add(cursor.getString(index = 0))
                })
            },
            parameters = 0,
            binders = null
        ).value.forEach { table ->
            sqlDriver.execute(
                identifier = null,
                sql = "DELETE FROM $table;",
                parameters = 0,
                binders = null
            ).value
        }
    }
}
