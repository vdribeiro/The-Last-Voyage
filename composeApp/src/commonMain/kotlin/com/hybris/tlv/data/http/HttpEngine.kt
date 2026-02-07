@file:ShadowedInTesting

package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import com.hybris.tlv.test.ShadowedInTesting

/**
 * Creates the http engine.
 */
internal expect fun createHttpEngine(): HttpClientEngine

/**
 * Check if internet is available.
 */
internal expect fun isInternetAvailable(): Boolean
