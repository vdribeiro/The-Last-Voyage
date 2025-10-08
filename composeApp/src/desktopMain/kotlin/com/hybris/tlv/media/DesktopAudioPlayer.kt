package com.hybris.tlv.media

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal class DesktopAudioPlayer: AudioPlayer() {

    private var player: MediaPlayer? = null
    private var currentIndex = -1

    override fun isPlaying(): Boolean = player?.status == MediaPlayer.Status.PLAYING

    override fun play() {
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex) ?: return
        val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath) ?: return
        player = MediaPlayer(Media(resourceUrl.toString())).apply {
            setOnEndOfMedia { this@DesktopAudioPlayer.play() }
            this.play()
        }
        currentIndex = nextIndex
    }

    override fun resume() {
        player?.play()
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
