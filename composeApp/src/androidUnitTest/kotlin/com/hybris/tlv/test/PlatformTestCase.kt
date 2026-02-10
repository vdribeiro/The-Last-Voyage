package com.hybris.tlv.test

import android.os.Binder
import androidx.test.core.app.ApplicationProvider
import com.hybris.tlv.applicationContext
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowServiceManager
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
internal actual abstract class PlatformTestCase actual constructor() {

    @Before
    fun setupContext() {
        applicationContext = ApplicationProvider.getApplicationContext()

        val mockBinder = Binder()
        ReflectionHelpers.callStaticMethod<Unit>(
            ShadowServiceManager::class.java,
            "addService",
            ReflectionHelpers.ClassParameter.from(String::class.java, "media_metrics"),
            ReflectionHelpers.ClassParameter.from(android.os.IBinder::class.java, mockBinder)
        )
    }
}
