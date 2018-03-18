package fr.xebia.simonthings.ui

import android.content.Context
import android.media.MediaPlayer
import fr.xebia.simonthings.R
import fr.xebia.simonthings.engine.GameInputButton

private val sounds = HashMap<GameInputButton, Int>().apply {
    put(GameInputButton.YELLOW, R.raw.r)
    put(GameInputButton.WHITE, R.raw.b)
}

class SoundManager(val context: Context) {
    var mediaPlayer: MediaPlayer? = null

    fun playSound(soundResId: Int) {
        stop()

        println("START SOUND")
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
    }

    fun playSound(gameInputButton: GameInputButton) {
        sounds[gameInputButton]?.let {
            playSound(it)
        }
    }

    fun stop() {
        println("STOP SOUND")
        mediaPlayer?.release()
        mediaPlayer = null
    }
}