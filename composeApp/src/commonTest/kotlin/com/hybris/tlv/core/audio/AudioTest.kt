package com.hybris.tlv.core.audio

import kotlin.test.Test
import com.hybris.tlv.test.TestCase

internal class AudioTest: TestCase() {
    @Test
    fun controls() = runUnitTest {
        pressAllActions(audioPlayer = AudioPlayer())
        pressAllActions(audioPlayer = createAudioPlayer())
    }

    private fun pressAllActions(audioPlayer: AudioPlayer) {
        audioPlayer.action(action = AudioPlayer.Action.Play(playlist = emptyList()))
        audioPlayer.action(action = AudioPlayer.Action.Pause)
        audioPlayer.action(action = AudioPlayer.Action.Resume)
        audioPlayer.action(action = AudioPlayer.Action.Toggle)
    }
}
