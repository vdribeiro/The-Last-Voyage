package com.hybris.tlv.test

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.test.core.app.ApplicationProvider
import com.hybris.tlv.applicationContext
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities

@RunWith(RobolectricTestRunner::class)
internal actual abstract class PlatformTestCase actual constructor() {

    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val shadowConnectivityManager = shadowOf(connectivityManager)
        val network = connectivityManager.activeNetwork
        val capabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(capabilities).apply {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        shadowConnectivityManager.setNetworkCapabilities(network, capabilities)
    }
}
