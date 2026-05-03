package com.hybris.tlv.core.audio

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.applicationContext

internal class AndroidAudioPlayer: AudioPlayer() {

    private val player: ExoPlayer = ExoPlayer.Builder(applicationContext).build()

    override fun isPlaying(): Boolean = player.isPlaying

    override fun play(loop: Boolean) {
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it.path}".toUri()) }
        player.apply {
            stop()
            setMediaItems(mediaItems)
            repeatMode = if (loop) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            prepare()
        }
        resume()
    }

    override fun resume() {
        if (enabled) player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }
}

internal actual fun createAudioPlayer(): AudioPlayer = AndroidAudioPlayer()
