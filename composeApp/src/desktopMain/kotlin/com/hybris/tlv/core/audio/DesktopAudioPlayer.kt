package com.hybris.tlv.core.audio

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal class DesktopAudioPlayer: AudioPlayer() {

    private var player: MediaPlayer? = null
    private var currentIndex = -1

    override fun isPlaying(): Boolean = player?.status == MediaPlayer.Status.PLAYING

    override fun play(loop: Boolean) {
        val nextIndex = currentIndex + 1
        if (!loop && nextIndex >= playlist.size) {
            stop()
            return
        }
        currentIndex = nextIndex % playlist.size

        val trackPath = playlist.getOrNull(index = currentIndex)?.path ?: throw Throwable("Unable to get track at index $currentIndex")
        val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath) ?: throw Throwable("Unable to get resource for $trackPath")

        player?.apply {
            stop()
            dispose()
        }
        player = MediaPlayer(Media(resourceUrl.toString())).apply {
            setOnEndOfMedia { this@DesktopAudioPlayer.play(loop = loop) }
            if (enabled) play()
        }
    }

    override fun resume() {
        if (enabled) player?.play()
    }

    override fun pause() {
        player?.pause()
    }

    override fun stop() {
        player?.stop()
        player?.dispose()
        player = null
        currentIndex = -1
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = DesktopAudioPlayer()
