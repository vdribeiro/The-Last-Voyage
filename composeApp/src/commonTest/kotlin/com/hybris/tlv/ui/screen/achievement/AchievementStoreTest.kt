package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.achievements
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val store: AchievementStore get() = storeFactory.createAchievementStore()

    @BeforeTest
    fun setup() = runBlocking {
        mockCore.sqlDriver.clearDatabase()
        mockCore.navigation?.navigate(screen = NavigationManager.Screen.ACHIEVEMENT) ?: Unit
    }

    @Test
    fun `init`() = runBlocking {
        mockCore.useCases.achievement.prepopulateAchievements()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        mockCore.useCases.achievement.prepopulateAchievements()
        store
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mockCore.navigation?.stateFlow?.value?.screen)
        mockCore.navigation?.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mockCore.navigation?.stateFlow?.value?.screen)
    }
}
