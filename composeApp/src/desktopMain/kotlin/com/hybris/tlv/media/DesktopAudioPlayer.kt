package com.hybris.tlv.media

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

internal class DesktopAudioPlayer: AudioPlayer() {

    private var player: MediaPlayer? = null

    override fun playNextTrack() {
        stop()
        val nextIndex = (currentIndex + 1) % playlist.size
        val trackPath = playlist.getOrNull(index = nextIndex) ?: return
        val resourceUrl = Thread.currentThread().contextClassLoader.getResource(trackPath) ?: return
        player = MediaPlayer(Media(resourceUrl.toString())).apply {
            setOnEndOfMedia { playNextTrack() }
            play()
        }
        currentIndex = nextIndex
    }

    override fun isPlaying(): Boolean = player?.status == MediaPlayer.Status.PLAYING

    override fun resumePlayer() {
        player?.play()
    }

    override fun pausePlayer() {
        player?.pause()
    }

    override fun stopPlayer() {
        player?.stop()
        player?.dispose()
        player = null
        currentIndex = -1
    }
}
