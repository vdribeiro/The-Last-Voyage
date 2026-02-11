@file:ExcludeFromTesting

package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import com.hybris.tlv.test.ExcludeFromTesting

internal actual fun createHttpEngine(): HttpClientEngine = Darwin.create()
