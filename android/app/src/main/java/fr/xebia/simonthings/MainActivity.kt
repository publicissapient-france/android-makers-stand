package fr.xebia.simonthings

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.*
import fr.xebia.simonthings.ui.NameFragment
import fr.xebia.simonthings.ui.PlayFragment
import fr.xebia.simonthings.ui.WaitingFragment


class MainActivity : Activity() {
    private val gameEngine = GameEngine()

    private val buttonsGpioPinNames = mutableListOf(Pair("BCM2", fr.xebia.simonthings.engine.Button.YELLOW), Pair("BCM3", fr.xebia.simonthings.engine.Button.WHITE))
    private val buttons = mutableListOf<Button>()

    private val ledGpioPinNames = mutableListOf(Pair("BCM5", fr.xebia.simonthings.engine.Button.YELLOW), Pair("BCM6", fr.xebia.simonthings.engine.Button.WHITE))
    private val leds = mutableListOf<Pair<fr.xebia.simonthings.engine.Button, Gpio>>()

    private lateinit var peripheralManager: PeripheralManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        peripheralManager = PeripheralManager.getInstance()

        buttonsGpioPinNames.forEach { buttonPair ->
            val button = Button(buttonPair.first, Button.LogicState.PRESSED_WHEN_LOW)
            button.setOnButtonEventListener { _, _ -> buttonPressed(buttonPair.first) }
            buttons.add(button)
        }

        ledGpioPinNames.forEach {
            val gpio = peripheralManager.openGpio(it.first)
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            gpio.setActiveType(Gpio.ACTIVE_HIGH)
            gpio.value = false
            leds.add(Pair(it.second, gpio))
        }

        setContentView(R.layout.activity_main)

        gameEngine.listen()
                .subscribe { instruction ->
                    when (instruction) {
                        is ScreenInstruction -> {
                            showScreen(instruction.screen)
                        }

                        is LedInstruction -> {
                            showLed(instruction.led, instruction.show)
                        }
                    }

                }

        gameEngine.start()
    }

    private fun showLed(led: fr.xebia.simonthings.engine.Button, show: Boolean) {
        println("Show led ${led.name} $show")
        leds.first {
            it.first == led
        }.second.value = show
    }

    private fun buttonPressed(buttonColor: String) {
        gameEngine.buttonPressed(buttonsGpioPinNames.first { it.first == buttonColor }.second)
    }

    private fun showScreen(screen: Screen) {
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
        fragment?.let { fragment ->
            fragmentManager.beginTransaction()?.apply {
                replace(R.id.frame, fragment)
                commit()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        buttons.forEach {
            it.close()
        }

        leds.forEach {
            it.second.close()
        }
    }
}
