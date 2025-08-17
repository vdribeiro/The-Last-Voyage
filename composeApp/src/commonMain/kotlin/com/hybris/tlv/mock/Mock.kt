package com.hybris.tlv.mock

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.AppCore
import com.hybris.tlv.Core
import com.hybris.tlv.database.SqlDriverFactory
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.EXOPLANET_ARCHIVE_URL
import com.hybris.tlv.http.client.setContentValidator
import com.hybris.tlv.http.client.setRequestUrl
import com.hybris.tlv.http.json.json
import com.hybris.tlv.http.json.loadFromJson
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.CommonLocalConfig
import com.hybris.tlv.storage.CommonRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.Navigation.Screen
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.credits.model.Credits
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.space.mapper.toExoplanetJson
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class Mock {

    val catastrophes: List<Catastrophe> by lazy {
        runBlocking { loadFromJson(path = "files/catastrophes.json") }
    }
    val engines: List<Engine> by lazy {
        runBlocking { loadFromJson(path = "files/engines.json") }
    }
    val stellarHosts: List<StellarHost> by lazy {
        runBlocking { loadFromJson(path = "files/hosts.json") }
    }
    val planets: List<Planet> by lazy {
        runBlocking { loadFromJson(path = "files/planets.json") }
    }
    val events: List<Event> by lazy {
        runBlocking { loadFromJson(path = "files/events.json") }
    }
    val achievements: List<Achievement> by lazy {
        runBlocking { loadFromJson(path = "files/achievements.json") }
    }
    val credits: List<Credits> by lazy {
        runBlocking { loadFromJson(path = "files/credits.json") }
    }

    val dispatcher: Dispatcher = CommonDispatchers()
    val locale: Locale = CommonLocale()
    val localConfig: LocalConfig = CommonLocalConfig()
    val remoteConfig: RemoteConfig = CommonRemoteConfig()
    val firestore: Firestore = CommonFirestore()
    val databaseDriver: SqlDriver = SqlDriverFactory.build(inMemory = true)
    val httpClient: HttpClient = buildExoplanetHttpClient()
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        firestore = firestore,
        databaseDriver = databaseDriver,
        httpClient = httpClient,
    )
    val core: Core = AppCore(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases,
    )
    val navigation = Navigation(core = core)

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
            core.prepopulate().last()
        }
    }

    fun setup() {}

    fun buildExoplanetHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            if (request.method == HttpMethod.Get && request.url.encodedPath.startsWith(prefix = "/sync")) {
                val stellarHostsMap = stellarHosts.associateBy { it.id }
                respond(
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = json.encodeToString(value = planets.mapNotNull {
                        val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                        it.toExoplanetJson(stellarHost = stellarHost)
                    }),
                )
            } else respondError(status = HttpStatusCode.NotFound, content = "Resource not found for path: ${request.url.encodedPath}")
        }

        return HttpClient(engine = mockEngine) {
            setRequestUrl(url = EXOPLANET_ARCHIVE_URL)
            setContentValidator()
        }
    }
}
