package com.hybris.tlv.core.audio

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.applicationContext

internal class AndroidAudioPlayer: AudioPlayer() {

    private val player: ExoPlayer = ExoPlayer.Builder(applicationContext).build()
    private var paused: Boolean = false

    override fun isPlaying(): Boolean = player.isPlaying

    override fun play() {
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it.path}".toUri()) }
        player.apply {
            setMediaItems(mediaItems)
            shuffleModeEnabled = true
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
        if (!paused) player.play()
    }

    override fun resume() {
        player.play()
        paused = false
    }

    override fun pause() {
        player.pause()
        paused = true
    }

    override fun stop() {
        player.stop()
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = AndroidAudioPlayer()
