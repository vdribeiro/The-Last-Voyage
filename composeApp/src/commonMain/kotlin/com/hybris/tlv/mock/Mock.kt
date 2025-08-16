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
import com.hybris.tlv.http.client.HttpClientFactory
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
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.last

internal class Mock {

    private val dispatcher: Dispatcher by lazy {
        CommonDispatchers()
    }
    private val locale: Locale by lazy {
        CommonLocale()
    }
    private val localConfig: LocalConfig by lazy {
        CommonLocalConfig()
    }
    private val remoteConfig: RemoteConfig by lazy {
        CommonRemoteConfig()
    }
    private val firestore: Firestore by lazy {
        CommonFirestore()
    }

    private val databaseDriver: SqlDriver by lazy {
        SqlDriverFactory.build()
    }

    private val httpClient: HttpClient by lazy {
        HttpClientFactory.buildExoplanetHttpClient()
    }
    private val useCases: UseCases by lazy {
        Gateways(
            dispatcher = dispatcher,
            firestore = firestore,
            databaseDriver = databaseDriver,
            httpClient = httpClient,
        )
    }
    private val core: Core by lazy {
        AppCore(
            dispatcher = dispatcher,
            locale = locale,
            localConfig = localConfig,
            remoteConfig = remoteConfig,
            useCases = useCases,
        )
    }
    private val navigation by lazy {
        Navigation(core = core)
    }

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    ) = navigation.Screen(
        screen = screen,
        state = state
    )

    suspend fun prepopulateDatabase() = core.prepopulate().last()
}
