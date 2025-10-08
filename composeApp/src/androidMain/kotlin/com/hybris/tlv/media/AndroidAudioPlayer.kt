package com.hybris.tlv.media

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.applicationContext

internal class AndroidAudioPlayer: AudioPlayer() {

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(applicationContext).build()
    }

    override fun isPlaying(): Boolean = player.isPlaying

    override fun play() {
        stop()
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it}".toUri()) }
        player.apply {
            setMediaItems(mediaItems)
            shuffleModeEnabled = true
        }
        resume()
    }

    override fun resume() {
        player.apply {
            if (mediaItemCount <= 0) return
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }
}
