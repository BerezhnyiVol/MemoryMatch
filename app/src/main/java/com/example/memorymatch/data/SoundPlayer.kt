package com.example.memorymatch.data

import android.content.Context
import android.media.MediaPlayer
import com.example.memorymatch.R

object SoundPlayer {

    fun playButtonSound(context: Context) {
        play(context, R.raw.button)
    }

    fun playStartSound(context: Context) {
        play(context, R.raw.start)
    }

    fun playCardFlipSound(context: Context) {
        play(context, R.raw.card)
    }

    fun playCorrectPairSound(context: Context) {
        play(context, R.raw.ok)
    }

    fun playWrongPairSound(context: Context) {
        play(context, R.raw.not_ok)
    }
    fun playGameOverSound(context: Context) {
        play(context, R.raw.game_over)
    }

    private fun play(context: Context, resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
}
