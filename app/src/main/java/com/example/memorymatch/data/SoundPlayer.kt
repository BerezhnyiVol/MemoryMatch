
    package com.example.memorymatch.data

    import android.content.Context
    import android.media.MediaPlayer
    import com.example.memorymatch.R

    object SoundPlayer {
        fun playButtonSound(context: Context) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.setOnCompletionListener { it.release() }
            mediaPlayer.start()
        }

        fun playStartSound(context: Context) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.start)
            mediaPlayer.setOnCompletionListener { it.release() }
            mediaPlayer.start()
        }
    }


