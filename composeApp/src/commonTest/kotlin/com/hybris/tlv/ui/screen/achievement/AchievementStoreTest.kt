package com.hybris.tlv.ui.screen.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.achievements
import com.hybris.tlv.getAchievementStore
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class AchievementStoreTest {

    private val store: AchievementStore get() = getAchievementStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = AchievementScreen))
    }

    @Test
    fun `init`() = runBlocking {
        useCases.achievement.syncAchievements()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        useCases.achievement.syncAchievements()
        store
//        assertEquals(expected = AchievementScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
