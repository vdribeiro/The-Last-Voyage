package com.hybris.tlv

import kotlin.test.assertEquals
import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class KInitializerTest {

    @Before
    fun setup() {
        applicationContext = ContextWrapper(null)
    }

    @Test
    fun initializer() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val initializer = KInitializer()
        initializer.create(context = context)
        assertEquals(expected = context, actual = applicationContext)
    }
}
