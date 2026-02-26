package com.hybris.tlv.ui.screen.catastrophe

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class CatastropheStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.catastrophe.syncCatastrophes()
        val store = storeFactory.get().getCatastropheStore()
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.selectedCatastrophe)
    }

    @Test
    fun initWithoutCatastrophes() = runUnitTest {
        assertNavigation(list = emptyList())
        storeFactory.get().getCatastropheStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun next() = runUnitTest {
        dependency.get().useCases.catastrophe.syncCatastrophes()
        val store = storeFactory.get().getCatastropheStore()
        assertNavigation(list = emptyList())
        store.send(action = CatastropheAction.Next)
        assertNavigation(list = listOf(Screen.Game()))
    }
}
