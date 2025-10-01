package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.achievements
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val store: AchievementStore get() = storeFactory.createAchievementStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testDependency.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Achievement)
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.achievement.prepopulateAchievements()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.achievement.prepopulateAchievements()
        store
        assertEquals(expected = NavigationManager.Screen.Achievement, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
