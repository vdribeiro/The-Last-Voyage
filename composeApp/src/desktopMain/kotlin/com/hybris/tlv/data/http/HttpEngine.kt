package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import com.hybris.tlv.test.ExcludeFromTesting

@ExcludeFromTesting
internal actual fun createHttpEngine(): HttpClientEngine = Java.create()
