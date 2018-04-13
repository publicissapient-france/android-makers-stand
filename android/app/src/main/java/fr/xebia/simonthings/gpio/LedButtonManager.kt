package fr.xebia.simonthings.gpio

import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.GameInputButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LedButtonManager {

    private val buttonPressSubject = PublishSubject.create<GameInputButton>()
    private val disposables = CompositeDisposable()

    fun init(peripheralManager: PeripheralManager, buttonPressedCallback: (GameInputButton) -> Unit) {
        CONTROLS.forEach {
            val button = Button(it.buttonGpioName, Button.LogicState.PRESSED_WHEN_LOW)
            button.setOnButtonEventListener { _, _ -> buttonPressSubject.onNext(it.gameInputButton) }
            it.button = button

            val led = peripheralManager.openGpio(it.ledGpioName)
            led.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            led.setActiveType(Gpio.ACTIVE_HIGH)
            led.value = false

            it.ledGpio = led
        }

        disposables.add(
                buttonPressSubject
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .throttleFirst(300, TimeUnit.MILLISECONDS)
                        .subscribe {
                            buttonPressedCallback(it)
                        })
    }

    fun release() {
        disposables.clear()

        CONTROLS.forEach {
            try {
                it.ledGpio?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                it.button?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showLed(led: GameInputButton, show: Boolean) {
        CONTROLS.firstOrNull {
            it.gameInputButton == led
        }?.ledGpio?.value = show
    }
}