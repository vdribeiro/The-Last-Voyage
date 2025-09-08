package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.Core
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class AchievementStoreTest {

    private val mock by lazy {
        Core(
            dispatcher = TestDispatchers(),
            sqlDriver = createSqlDriver(inMemory = true),
            httpClient = HttpClientFactory.buildHttpClient()
        )
    }
    private val store
        get() = AchievementStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = AchievementState(),
            achievementUseCases = mock.useCases.achievement
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.ACHIEVEMENT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.sync.sync().last()
        val achievementStore = store
        assertEquals(expected = achievements, actual = achievementStore.stateFlow.value.achievements)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        store
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
