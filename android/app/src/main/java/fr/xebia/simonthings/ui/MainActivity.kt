package fr.xebia.simonthings.ui

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import fr.xebia.simonthings.R
import fr.xebia.simonthings.engine.Screen

class MainActivity : Activity() {

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        presenter.init()
    }

    fun showScreen(screen: Screen) {
        val fragment: Fragment? = when (screen) {
            Screen.WAITING -> {
                WaitingFragment()
            }
            Screen.NAME -> {
                NameFragment()
            }
            Screen.PLAY -> {
                PlayFragment()
            }
        }
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frame, fragment)
        ft.commit()
    }

    override fun onPause() {
        super.onPause()

        presenter.release()
    }
}



