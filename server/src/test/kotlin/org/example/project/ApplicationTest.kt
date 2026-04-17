package org.example.project

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.TestResult
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import com.hybris.tlv.module

class ApplicationTest {

    @Test
    fun testRoot(): TestResult = testApplication {
        application {
            module()
        }
        val response = client.get(urlString = "/")
        assertEquals(expected = HttpStatusCode.OK, actual = response.status)
        assertEquals(expected = "Ktor Server", actual = response.bodyAsText())
    }
}
