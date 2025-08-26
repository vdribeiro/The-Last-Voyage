package com.hybris.tlv.ui.screen.learn

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class LearnStoreTest {

    private val mock = Mock()
    private val store
        get() = LearnStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = LearnState(),
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.LEARN)
    }

    @Test
    fun `init`() = runBlocking {
        val exploreStore = store
        assertEquals(expected = Content.MENU, actual = exploreStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action change content`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.LEARN, actual = mock.navigation.stateFlow.value.screen)
        val learnStore = store
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.HostTypes)
        assertEquals(expected = Content.HOST_TYPES, actual = learnStore.stateFlow.value.currentContent)
        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.PlanetTypes)
        assertEquals(expected = Content.PLANET_TYPES, actual = learnStore.stateFlow.value.currentContent)
        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.Properties)
        assertEquals(expected = Content.PROPERTIES, actual = learnStore.stateFlow.value.currentContent)
        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.Mechanics)
        assertEquals(expected = Content.MECHANICS, actual = learnStore.stateFlow.value.currentContent)
        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.Habitability)
        assertEquals(expected = Content.HABITABILITY, actual = learnStore.stateFlow.value.currentContent)
        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)

        learnStore.send(action = LearnAction.Back)
        assertEquals(expected = Content.MENU, actual = learnStore.stateFlow.value.currentContent)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }
}
