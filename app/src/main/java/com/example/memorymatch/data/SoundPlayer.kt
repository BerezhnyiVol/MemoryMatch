package com.example.memorymatch.data

import android.content.Context
import android.media.MediaPlayer
import com.example.memorymatch.R

// Popis: Trieda zabezpečujúca prehrávanie zvukových efektov v aplikácii
object SoundPlayer {

    // Popis: Prehrá zvuk pri stlačení tlačidla
    fun playButtonSound(context: Context) {
        play(context, R.raw.button)
    }

    // Popis: Prehrá zvuk pri štarte hry
    fun playStartSound(context: Context) {
        play(context, R.raw.start)
    }

    // Popis: Prehrá zvuk pri otočení karty
    fun playCardFlipSound(context: Context) {
        play(context, R.raw.card)
    }

    // Popis: Prehrá zvuk pri správnom páre kariet
    fun playCorrectPairSound(context: Context) {
        play(context, R.raw.ok)
    }

    // Popis: Prehrá zvuk pri nesprávnom páre kariet
    fun playWrongPairSound(context: Context) {
        play(context, R.raw.not_ok)
    }

    // Popis: Prehrá zvuk pri konci hry
    fun playGameOverSound(context: Context) {
        play(context, R.raw.game_over)
    }

    // Popis: Všeobecná pomocná metóda na prehrávanie zvuku podľa ID zdroja
    private fun play(context: Context, resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
}
