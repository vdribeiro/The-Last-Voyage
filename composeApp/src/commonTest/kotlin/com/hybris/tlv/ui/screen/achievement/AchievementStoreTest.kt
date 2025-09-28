package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.achievements
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val store: AchievementStore get() = storeFactory.createAchievementStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testCore.navigation.navigate(screen = NavigationManager.Screen.Achievement)
    }

    @Test
    fun `init`() = runBlocking {
        testCore.useCases.achievement.prepopulateAchievements()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        testCore.useCases.achievement.prepopulateAchievements()
        store
        assertEquals(expected = NavigationManager.Screen.Achievement, actual = testCore.navigation.stateFlow.value.screen)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testCore.navigation.stateFlow.value.screen)
    }
}
