@file:ExcludeFromTesting

package com.hybris.tlv.core.audio

import com.hybris.tlv.test.ExcludeFromTesting
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal class DesktopAudioPlayer: AudioPlayer() {

    private var player: MediaPlayer? = null
    private var currentIndex = -1
    private var paused: Boolean = false

    override fun isPlaying(): Boolean = player?.status == MediaPlayer.Status.PLAYING

    override fun play() {
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex)?.path ?: throw Throwable("Unable to get track at index $nextIndex")
        val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath) ?: throw Throwable("Unable to get resource for $trackPath")
        player = MediaPlayer(Media(resourceUrl.toString())).apply {
            setOnEndOfMedia { this@DesktopAudioPlayer.play() }
            if (!paused) play()
        }
        currentIndex = nextIndex
    }

    override fun resume() {
        player?.play()
        paused = false
    }

    override fun pause() {
        player?.pause()
        paused = true
    }

    override fun stop() {
        player?.stop()
        player?.dispose()
        player = null
        currentIndex = -1
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = DesktopAudioPlayer()
