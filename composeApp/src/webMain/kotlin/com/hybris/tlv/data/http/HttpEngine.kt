package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

internal actual fun createHttpEngine(): HttpClientEngine = Js.create()
