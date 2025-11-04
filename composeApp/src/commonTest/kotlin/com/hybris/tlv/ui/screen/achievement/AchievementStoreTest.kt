package com.hybris.tlv.ui.screen.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.achievements
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class AchievementStoreTest {

    private val store: AchievementStore get() = testDependency.storeFactory.createAchievementStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Splash))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.MainMenu))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Achievement))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.achievement.syncAchievements()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.achievement.syncAchievements()
        store
        assertEquals(expected = Screen.Achievement, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
