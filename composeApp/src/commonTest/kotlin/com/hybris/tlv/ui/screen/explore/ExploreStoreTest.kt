package com.hybris.tlv.ui.screen.explore

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ExploreStoreTest {

    private val mock = Mock()
    private val store
        get() = ExploreStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = ExploreState(),
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.EXPLORE)
    }

    @Test
    fun `init`() = runBlocking {
        val exploreStore = store
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action change content`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.EXPLORE, actual = mock.navigation.stateFlow.value.screen)
        val exploreStore = store
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Mechanics)
        assertEquals(expected = Content.MECHANICS, actual = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Habitability)
        assertEquals(expected = Content.HABITABILITY, actual = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.PlanetTypes)
        assertEquals(expected = Content.PLANET_TYPES, actual = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Back)
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
