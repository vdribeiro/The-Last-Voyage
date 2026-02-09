@file:ShadowedInTesting

package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun createHttpEngine(): HttpClientEngine = Java.create()
