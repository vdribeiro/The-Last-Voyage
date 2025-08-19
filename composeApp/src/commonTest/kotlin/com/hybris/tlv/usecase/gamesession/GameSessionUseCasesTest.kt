package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

internal class GameSessionUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `game`() = runBlocking {
    }
}
