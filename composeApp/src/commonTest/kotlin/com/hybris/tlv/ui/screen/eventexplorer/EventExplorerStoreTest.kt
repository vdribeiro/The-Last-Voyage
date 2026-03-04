package com.hybris.tlv.ui.screen.eventexplorer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.flow.firstOrNull
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class EventExplorerStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.event.syncEvents()
        val store = storeFactory.get().getEventExplorerStore()
        store.stateFlow.firstOrNull() // Trigger observe
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.events.get().sortedBy { it.id }, actual = store.state.events.sortedBy { it.id })
    }
}
