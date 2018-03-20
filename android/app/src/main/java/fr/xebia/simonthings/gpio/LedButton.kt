package fr.xebia.simonthings.gpio

import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.pio.Gpio
import fr.xebia.simonthings.engine.GameInputButton

data class LedButton(
        val ledGpioName: String,
        val buttonGpioName: String,
        val gameInputButton: GameInputButton,
        var button: Button? = null,
        var ledGpio: Gpio? = null
)
