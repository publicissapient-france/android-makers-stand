package fr.xebia.simonthings.gpio

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.GameInputButton

class LedButtonManager {

    fun init(peripheralManager: PeripheralManager, buttonPressedCallback: (GameInputButton) -> Unit) {
        CONTROLS.forEach {
            val button = com.google.android.things.contrib.driver.button.Button(it.buttonGpioName, com.google.android.things.contrib.driver.button.Button.LogicState.PRESSED_WHEN_LOW)
            button.setOnButtonEventListener { _, _ -> buttonPressedCallback(it.gameInputButton) }
            it.button = button

            val led = peripheralManager.openGpio(it.ledGpioName)
            led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            led.setActiveType(Gpio.ACTIVE_HIGH)
            led.value = false

            it.ledGpio = led
        }
    }

    fun release() {
        CONTROLS.forEach {
            it.ledGpio?.close()
            it.button?.close()
        }
    }

    fun showLed(led: GameInputButton, show: Boolean) {
        CONTROLS.firstOrNull {
            it.gameInputButton == led
        }?.ledGpio?.value = show
    }
}