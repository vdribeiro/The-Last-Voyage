package com.hybris.tlv.core.audio

import kotlin.test.Test
import com.hybris.tlv.test.TestCase

internal class AudioTest: TestCase() {
    @Test
    fun playerControls() = runUnitTest {
        val audioPlayer = createAudioPlayer()
    }
}
