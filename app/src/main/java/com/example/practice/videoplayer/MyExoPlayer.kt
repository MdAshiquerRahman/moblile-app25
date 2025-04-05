package com.example.practice.videoplayer

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object MyExoPlayer {

    private var exoPlayer: ExoPlayer? = null

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }

    fun initPlayer(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun startPlaying(context: Context,videoUrl: String) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()
    }

    fun pause() {
        // Save the position before pausing
        exoPlayer?.pause()
    }

    fun stop() {
        // Save the position before stopping
        exoPlayer?.stop()
    }

    fun release() {
        // Save the last position before releasing
        exoPlayer?.release()
        exoPlayer = null
    }

}
