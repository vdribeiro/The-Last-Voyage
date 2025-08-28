package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.locale.AndroidLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.Storage
import com.hybris.tlv.storage.StorageManager
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases

private val dispatcher: Dispatcher by lazy {
    Dispatchers()
}
private val locale: Locale by lazy {
    AndroidLocale(context = applicationContext)
}
private val sqlDriver: SqlDriver by lazy {
    createSqlDriver()
}

private val useCases: UseCases by lazy {
    Gateways(
        dispatcher = dispatcher,
        locale = locale,
        storage = storage,
        sqlDriver = sqlDriver,
    )
}
private val navigation: NavigationManager by lazy {
    Navigation(
        dispatcher = dispatcher,
        locale = locale,
        storage = storage,
        useCases = useCases
    )
}

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App(navigation = navigation) }
    }
}
