package fr.xebia.simonthings.ui

import android.content.Context
import android.media.MediaPlayer
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import fr.xebia.simonthings.R
import fr.xebia.simonthings.engine.GameInputButton
import fr.xebia.simonthings.ui.SoundManager.Companion.NOTE_C4
import fr.xebia.simonthings.ui.SoundManager.Companion.NOTE_D4
import fr.xebia.simonthings.ui.SoundManager.Companion.NOTE_E4
import fr.xebia.simonthings.ui.SoundManager.Companion.NOTE_F4
import java.io.IOException


private val sounds = HashMap<GameInputButton, Pair<Int, Double>>().apply {
    put(GameInputButton.GREEN, Pair(R.raw.g, NOTE_C4.toDouble()))
    put(GameInputButton.RED, Pair(R.raw.r, NOTE_D4.toDouble()))
    put(GameInputButton.BLUE, Pair(R.raw.b, NOTE_E4.toDouble()))
    put(GameInputButton.YELLOW, Pair(R.raw.y, NOTE_F4.toDouble()))
}

class SoundManager(private val context: Context) {

    var speaker: Pwm? = null

    companion object {
        var NOTE_C4 = 262
        var NOTE_CS4 = 277
        var NOTE_D4 = 294
        var NOTE_DS4 = 311
        var NOTE_E4 = 330
        var NOTE_F4 = 349
        var NOTE_FS4 = 370
        var NOTE_G4 = 392
        var NOTE_GS4 = 415
        var NOTE_A4 = 440
        var NOTE_AS4 = 466
        var NOTE_B4 = 494
        var NOTE_C5 = 523
        var NOTE_CS5 = 554
        var NOTE_D5 = 587
        var NOTE_DS5 = 622
        var NOTE_E5 = 659
        var NOTE_F5 = 698
        var NOTE_FS5 = 740
        var NOTE_G5 = 784
        var NOTE_GS5 = 831
        var NOTE_A5 = 880
        var NOTE_AS5 = 932
        var NOTE_B5 = 988
        var NOTE_C6 = 1047
    }

    private var mediaPlayer: MediaPlayer? = null

    fun playSound(soundResId: Int, freq: Double) {
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
        }
        mediaPlayer?.start()

        if (freq > 0) {
            println("speaker is starting with freq : $freq")

            try {
                speaker?.setPwmFrequencyHz(freq / 10)
                speaker?.setPwmDutyCycle(20.0)
                speaker?.setEnabled(true)
            } catch (e: IOException) {
                println("Error speaker ${e.message}")

                e.printStackTrace()
            }
        }
    }

    fun playSound(gameInputButton: GameInputButton) {
        sounds[gameInputButton]?.let {
            playSound(it.first, it.second)
        }
    }

    fun release() {
        println("speaker is released")

        speaker?.close()
        speaker = null
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null

        println("speaker is stopping")
        speaker?.setEnabled(false)
    }

    fun init() {
        try {
            speaker = PeripheralManager.getInstance().openPwm("PWM1")
        } catch (e: IOException) {
            println("Error speaker ${e.message}")
            e.printStackTrace()
        }
    }
}