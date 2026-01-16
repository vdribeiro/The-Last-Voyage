package com.hybris.tlv.ui.screen.catastrophe

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class CatastropheStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.catastrophe.syncCatastrophes()
        val store = TestCase.storeFactory.getCatastropheStore()
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.selectedCatastrophe)
    }

    @Test
    fun initWithoutCatastrophes() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getCatastropheStore()
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun next() = TestCase.runUnitTest {
        TestCase.useCases.catastrophe.syncCatastrophes()
        val store = TestCase.storeFactory.getCatastropheStore()
        TestCase.assertNavigation(list = emptyList())
        store.send(action = CatastropheAction.Next)
        TestCase.assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Catastrophe)
        TestCase.assertNavigation(list = listOf(Screen.Catastrophe))
        TestCase.useCases.catastrophe.syncCatastrophes()
        TestCase.storeFactory.getCatastropheStore().back()
        TestCase.assertNavigation(list = listOf(Screen.Catastrophe))
    }
}
