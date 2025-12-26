package com.hybris.tlv.screen.mainmenu

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype

internal class MainMenuStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getMainMenuStore()
        assertTrue(actual = store.state.ongoingGameSession)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.ongoingGameSession)
    }
}
