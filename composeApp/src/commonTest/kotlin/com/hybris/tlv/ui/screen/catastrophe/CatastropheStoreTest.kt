package com.hybris.tlv.ui.screen.catastrophe

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class CatastropheStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        getUseCases().catastrophe.syncCatastrophes()
        val store = getStoreFactory().getCatastropheStore()
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.selectedCatastrophe)
    }

    @Test
    fun initWithoutCatastrophes() = runUnitTest {
        assertNavigation(list = emptyList())
        getStoreFactory().getCatastropheStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun next() = runUnitTest {
        getUseCases().catastrophe.syncCatastrophes()
        val store = getStoreFactory().getCatastropheStore()
        assertNavigation(list = emptyList())
        store.send(action = CatastropheAction.Next)
        assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Catastrophe)
        assertNavigation(list = listOf(Screen.Catastrophe))
        getUseCases().catastrophe.syncCatastrophes()
        getStoreFactory().getCatastropheStore().back()
        assertNavigation(list = listOf(Screen.Catastrophe))
    }
}
