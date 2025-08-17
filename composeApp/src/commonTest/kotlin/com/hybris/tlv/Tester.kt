package com.hybris.tlv

import com.hybris.tlv.database.Database
import com.hybris.tlv.http.client.ExoPlanetClient
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.achievement.local.AchievementDao
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.remote.AchievementApi
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote
import com.hybris.tlv.usecase.credits.local.CreditsDao
import com.hybris.tlv.usecase.credits.local.CreditsLocal
import com.hybris.tlv.usecase.credits.remote.CreditsApi
import com.hybris.tlv.usecase.credits.remote.CreditsRemote
import com.hybris.tlv.usecase.earth.local.EarthDao
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.remote.EarthApi
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import com.hybris.tlv.usecase.event.local.EventDao
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.remote.EventApi
import com.hybris.tlv.usecase.event.remote.EventRemote
import com.hybris.tlv.usecase.gamesession.local.GameSessionDao
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.ship.local.ShipDao
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.remote.ShipApi
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import com.hybris.tlv.usecase.space.local.SpaceDao
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.remote.SpaceApi
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.translation.local.TranslationDao
import com.hybris.tlv.usecase.translation.local.TranslationLocal
import com.hybris.tlv.usecase.translation.remote.TranslationApi
import com.hybris.tlv.usecase.translation.remote.TranslationRemote

internal abstract class Tester {
    
    protected val mock = Mock()

    protected val database by lazy {
        Database(driver = mock.sqlDriver).database
    }

    protected val httpClient by lazy {
        mock.httpClientFactory.buildExoplanetHttpClient()
    }

    protected val exoplanetHttpClient by lazy {
        ExoPlanetClient(httpClient = httpClient)
    }

    protected val translationDao: TranslationLocal by lazy {
        TranslationDao(database = database)
    }

    protected val earthDao: EarthLocal by lazy {
        EarthDao(database = database)
    }

    protected val shipDao: ShipLocal by lazy {
        ShipDao(database = database)
    }

    protected val spaceDao: SpaceLocal by lazy {
        SpaceDao(database = database)
    }

    protected val eventDao: EventLocal by lazy {
        EventDao(database = database)
    }

    protected val gameSessionDao: GameSessionLocal by lazy {
        GameSessionDao(database = database)
    }

    protected val achievementDao: AchievementLocal by lazy {
        AchievementDao(database = database)
    }

    protected val creditsDao: CreditsLocal by lazy {
        CreditsDao(database = database)
    }

    protected val translationApi: TranslationRemote by lazy {
        TranslationApi(firestore = mock.firestore)
    }

    protected val earthApi: EarthRemote by lazy {
        EarthApi(firestore = mock.firestore)
    }

    protected val shipApi: ShipRemote by lazy {
        ShipApi(firestore = mock.firestore)
    }

    protected val spaceApi: SpaceRemote by lazy {
        SpaceApi(
            exoplanetHttpClient = exoplanetHttpClient,
            firestore = mock.firestore
        )
    }

    protected val eventApi: EventRemote by lazy {
        EventApi(firestore = mock.firestore)
    }

    protected val achievementApi: AchievementRemote by lazy {
        AchievementApi(firestore = mock.firestore)
    }

    protected val creditsApi: CreditsRemote by lazy {
        CreditsApi(firestore = mock.firestore)
    }
}
