package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.domain.usecase.credit.model.CreditType
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CreditStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.credit.prepopulateCredits()
        val store = storeFactory.get().getCreditStore()
        assertFalse(actual = store.state.loading)
        val credits = FakeData.credits.get().groupBy { it.type }
        assertEquals(expected = credits[CreditType.CREATOR], actual = store.state.creators)
        assertEquals(expected = credits[CreditType.SOURCE], actual = store.state.sources)
        assertEquals(expected = credits[CreditType.MUSIC], actual = store.state.musics)
        assertEquals(expected = credits[CreditType.SUPPORTER], actual = store.state.supporters)
    }

    @Test
    fun initWithoutCredits() = runUnitTest {
        val store = storeFactory.get().getCreditStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.creators.isEmpty())
        assertTrue(actual = store.state.sources.isEmpty())
        assertTrue(actual = store.state.musics.isEmpty())
        assertTrue(actual = store.state.supporters.isEmpty())
    }
}
