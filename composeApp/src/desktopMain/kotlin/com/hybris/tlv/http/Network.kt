package com.hybris.tlv.http

import com.hybris.tlv.telemetry.Telemetry
import java.net.HttpURLConnection
import java.net.URI

internal actual fun isInternetAvailable(): Boolean = runCatching {
    val url = URI("https://clients3.google.com/generate_204").toURL()
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 1500
    connection.readTimeout = 1500
    connection.useCaches = false
    connection.instanceFollowRedirects = false
    connection.responseCode == HttpURLConnection.HTTP_NO_CONTENT
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it)
    false
}

private const val TAG = "Network"
