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
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it}".toUri()) }
        player.apply {
            setMediaItems(mediaItems)
            shuffleModeEnabled = true
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
        resume()
    }

    override fun resume() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }
}
