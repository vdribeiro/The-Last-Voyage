package com.hybris.tlv.mock

import androidx.compose.runtime.Composable
import com.hybris.tlv.database.CommonSqlDriverFactory
import com.hybris.tlv.database.SqlDriverFactory
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.CommonHttpClientFactory
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.http.json.loadFromJson
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.CommonLocalConfig
import com.hybris.tlv.storage.CommonRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.credits.model.Credits
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal val catastrophes: List<Catastrophe> by lazy {
    runBlocking { loadFromJson(path = "files/catastrophes.json") }
}
internal val engines: List<Engine> by lazy {
    runBlocking { loadFromJson(path = "files/engines.json") }
}
internal val stellarHosts: List<StellarHost> by lazy {
    runBlocking { loadFromJson(path = "files/hosts.json") }
}
internal val planets: List<Planet> by lazy {
    runBlocking { loadFromJson(path = "files/planets.json") }
}
internal val events: List<Event> by lazy {
    runBlocking { loadFromJson(path = "files/events.json") }
}
internal val achievements: List<Achievement> by lazy {
    runBlocking { loadFromJson(path = "files/achievements.json") }
}
internal val credits: List<Credits> by lazy {
    runBlocking { loadFromJson(path = "files/credits.json") }
}

internal class Mock {

    val dispatcher: Dispatcher = CommonDispatchers()
    val locale: Locale = CommonLocale()
    val localConfig: LocalConfig = CommonLocalConfig()
    val remoteConfig: RemoteConfig = CommonRemoteConfig()
    val firestore: Firestore = CommonFirestore()
    val sqlDriverFactory: SqlDriverFactory = CommonSqlDriverFactory()
    val httpClientFactory: HttpClientFactory = CommonHttpClientFactory()
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        firestore = firestore,
        sqlDriverFactory = sqlDriverFactory,
        httpClientFactory = httpClientFactory
    )
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    ) = navigation.Screen(
        screen = screen,
        state = state
    )

    init {
        runBlocking {
            useCases.sync.prepopulate().last()
        }
    }
}
