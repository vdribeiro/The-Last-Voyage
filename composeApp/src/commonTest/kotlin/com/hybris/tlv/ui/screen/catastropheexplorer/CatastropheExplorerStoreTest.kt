package com.hybris.tlv.ui.screen.catastropheexplorer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CatastropheExplorerStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.catastrophe.syncCatastrophes()
        val store = storeFactory.get().getCatastropheExplorerStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.catastrophes.get().sortedBy { it.id }, actual = store.state.catastrophes.sortedBy { it.id })
    }
}
