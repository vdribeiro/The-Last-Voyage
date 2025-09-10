package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.achievements
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val store by lazy {
        AchievementStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = AchievementState(),
            achievementUseCases = mock.useCases.achievement
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.sync.sync().last()
        val achievementStore = store.apply { setup(state = AchievementState()) }
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        store.setup(state = AchievementState())
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
