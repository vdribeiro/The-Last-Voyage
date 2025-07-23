package com.hybris.tlv.media

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.hybris.tlv.logger.Logger

internal class AndroidAudioPlayer(context: Context): AudioPlayer {

    private val player = ExoPlayer.Builder(context).build()
    private val playlist = mutableListOf<String>()

    override fun play(vararg playlist: String) = runCatching {
        if (this.playlist.sorted() == playlist.toList().sorted()) return@runCatching
        this.playlist.apply {
            clear()
            addAll(elements = playlist)
        }
        stop()
        val mediaItems = playlist.map { MediaItem.fromUri("asset:///${it}".toUri()) }
        player.setMediaItems(mediaItems)
        player.shuffleModeEnabled = true
        resume()
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error playing media: ${it.message}")
    }

    override fun resume() = runCatching {
        player.apply {
            if (mediaItemCount <= 0) return@runCatching
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = "Error resuming media: ${it.message}")
    }

    override fun pause() = runCatching { player.pause() }.getOrElse {
        Logger.error(tag = TAG, message = "Error pausing media: ${it.message}")
    }

    override fun stop() = runCatching { player.stop() }.getOrElse {
        Logger.error(tag = TAG, message = "Error stopping media: ${it.message}")
    }

    override fun release() = runCatching { player.release() }.getOrElse {
        Logger.error(tag = TAG, message = "Error releasing media: ${it.message}")
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}

@Composable
internal actual fun rememberAudioPlayer(): AudioPlayer {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    val player = remember { AndroidAudioPlayer(context = context) }

    DisposableEffect(key1 = lifecycleOwner, key2 = player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> player.resume()
                Lifecycle.Event.ON_PAUSE -> player.pause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    return player
}
