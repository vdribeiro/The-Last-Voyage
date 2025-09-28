package com.hybris.tlv.ui.screen.tutorial

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class TutorialStoreTest {

    private val store: TutorialStore get() = storeFactory.createTutorialStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testCore.navigation.navigate(screen = NavigationManager.Screen.Tutorial)
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
    }
}
