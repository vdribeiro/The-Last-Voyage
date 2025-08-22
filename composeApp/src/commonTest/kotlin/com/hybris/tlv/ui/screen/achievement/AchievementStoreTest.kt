package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val mock = Mock()
    private val store
        get() = AchievementStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = AchievementState(),
            achievementUseCases = mock.useCases.achievement
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalAchievement.syncAchievements()
        val achievementStore = store
        assertEquals(actual = achievements, expected = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.internalAchievement.syncAchievements()
        val achievementStore = store
        assertEquals(actual = NavigationManager.Screen.ACHIEVEMENT, expected = mock.navigation.stateFlow.value.screen)
        achievementStore.send(action = AchievementAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
