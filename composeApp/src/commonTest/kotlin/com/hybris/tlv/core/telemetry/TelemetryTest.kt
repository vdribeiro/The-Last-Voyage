package com.hybris.tlv.core.telemetry

import kotlin.test.Test
import com.hybris.tlv.test.TestCase

internal class TelemetryTest: TestCase() {

    @Test
    fun print() = runUnitTest {
        Telemetry.engine = null
        printAll()
        Telemetry.engine = MockLogger()
        printAll()
        Telemetry.engine = Logger()
        printAll()
    }

    private fun printAll() {
        Telemetry.info(tag = "Telemetry", message = "Info test")
        Telemetry.error(tag = "Telemetry", message = "Error test")
        Telemetry.feedback(message = "Feedback test")
    }
}