package fr.xebia.simonthings.ui

import android.content.Context
import android.media.MediaPlayer
import fr.xebia.simonthings.R
import fr.xebia.simonthings.engine.GameInputButton

private val sounds = HashMap<GameInputButton, Int>().apply {
    put(GameInputButton.YELLOW, R.raw.y)
    put(GameInputButton.BLUE, R.raw.b)
    put(GameInputButton.RED, R.raw.r)
    put(GameInputButton.GREEN, R.raw.g)
}

class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(soundResId: Int) {
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
        }
        mediaPlayer?.start()
    }

    fun playSound(gameInputButton: GameInputButton) {
        sounds[gameInputButton]?.let {
            playSound(it)
        }
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}