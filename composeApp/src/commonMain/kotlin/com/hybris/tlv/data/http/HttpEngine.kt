@file:ExcludeFromTesting

package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Creates the http engine.
 */
internal expect fun createHttpEngine(): HttpClientEngine
