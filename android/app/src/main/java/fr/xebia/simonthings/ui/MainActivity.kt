package fr.xebia.simonthings.ui

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import fr.xebia.simonthings.R
import fr.xebia.simonthings.engine.GameInputButton
import fr.xebia.simonthings.engine.Screen
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val presenter = MainPresenter(this, SoundManager(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        presenter.init()

        player_name.setOnEditorActionListener { textView, p1, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                val input = textView.text.toString()
                if (input.trim().replace("\n", "").isEmpty()) {
                    return@setOnEditorActionListener false
                }

                presenter.onNameEntered(input)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun showScreen(screen: Screen) {
        when (screen) {
            Screen.NAME -> {

                button_red.visibility = View.GONE
                button_blue.visibility = View.GONE
                button_green.visibility = View.GONE
                button_yellow.visibility = View.GONE

                score.text = ""
                score.visibility = View.GONE
                player_name.text = null
                player_name.visibility = View.VISIBLE
            }
            Screen.PLAY -> {

                button_red.visibility = View.VISIBLE
                button_blue.visibility = View.VISIBLE
                button_green.visibility = View.VISIBLE
                button_yellow.visibility = View.VISIBLE

                player_name.visibility = View.GONE
                score.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()

        presenter.release()
    }

    fun showLedPress(gameInputButton: GameInputButton, show: Boolean) {
        runOnUiThread {
            val view = when (gameInputButton) {
                GameInputButton.RED -> button_red
                GameInputButton.GREEN -> button_green
                GameInputButton.YELLOW -> button_yellow
                GameInputButton.BLUE -> button_blue
            }

            if (show) {
                view.alpha = 1f
            } else {
                view.alpha = 0.1f
            }
        }
    }

    fun showScore(scoreNumber: Int) {
        runOnUiThread {
            score.text = "$scoreNumber"
        }
    }
}



