package com.hybris.tlv.logger

import kotlin.system.measureTimeMillis

internal inline fun benchmark(message: String, block: () -> Unit) {
    val time = measureTimeMillis { block() }
    Logger.debug(tag = "Benchmark", message = "$message: ${time / 60000.0} minutes")
}
