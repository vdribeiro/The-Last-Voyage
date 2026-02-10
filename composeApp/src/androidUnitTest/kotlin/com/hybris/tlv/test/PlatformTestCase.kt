package com.hybris.tlv.test

import androidx.test.core.app.ApplicationProvider
import com.hybris.tlv.applicationContext
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal actual abstract class PlatformTestCase actual constructor() {

    @Before
    fun setupContext() {
        applicationContext = ApplicationProvider.getApplicationContext()
    }
}
