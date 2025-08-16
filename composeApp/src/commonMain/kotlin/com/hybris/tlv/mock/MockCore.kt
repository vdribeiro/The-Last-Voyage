package com.hybris.tlv.mock

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.AppCore
import com.hybris.tlv.Core
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.HttpClientFactory.setContentValidator
import com.hybris.tlv.http.client.HttpClientFactory.setRequestUrl
import com.hybris.tlv.http.client.json
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
import com.hybris.tlv.usecase.space.mapper.toExoplanetJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal class MockCore(driver: SqlDriver) {
    private val dispatcher: Dispatcher = CommonDispatchers()
    private val locale: Locale = CommonLocale()
    private val localConfig: LocalConfig = CommonLocalConfig()
    private val remoteConfig: RemoteConfig = CommonRemoteConfig()
    private val firestore: Firestore = CommonFirestore()
    private val httpClient: HttpClient = buildExoplanetMockHttpClient()
    private val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        firestore = firestore,
        databaseDriver = driver,
        httpClient = httpClient,
    )

    private val core: Core = AppCore(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases,
    )
    private val navigation = Navigation(core = core)

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    ) = navigation.Screen(
        screen = screen,
        state = state
    )

    private fun buildExoplanetMockHttpClient(): HttpClient {
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
            setRequestUrl(url = "exoplanetarchive.ipac.caltech.edu/TAP")
            setContentValidator()
        }
    }
}
