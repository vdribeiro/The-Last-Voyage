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
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)
    }

    @Test
    fun navigate() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.EXPLORE, expected = mock.navigation.stateFlow.value.screen)
        val exploreStore = store
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Mechanics)
        assertEquals(actual = Content.MECHANICS, expected = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Habitability)
        assertEquals(actual = Content.HABITABILITY, expected = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.PlanetTypes)
        assertEquals(actual = Content.PLANET_TYPES, expected = exploreStore.stateFlow.value.currentContent)
        exploreStore.send(action = ExploreAction.Back)
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)

        exploreStore.send(action = ExploreAction.Back)
        assertEquals(actual = Content.MENU, expected = exploreStore.stateFlow.value.currentContent)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
