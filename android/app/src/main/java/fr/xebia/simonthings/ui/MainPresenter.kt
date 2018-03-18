package fr.xebia.simonthings.ui

import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.*
import fr.xebia.simonthings.gpio.LedButtonManager
import io.reactivex.disposables.CompositeDisposable


class MainPresenter(private val view: MainActivity, private val soundManager: SoundManager) {

    private val gameEngine = GameEngine()
    private val ledButtonsManager = LedButtonManager()
    private val disposables = CompositeDisposable()

    fun init() {
        ledButtonsManager.init(PeripheralManager.getInstance(), this::buttonPressed)

        disposables.add(gameEngine.listen()
                .subscribe { instruction ->
                    when (instruction) {
                        is ScreenDisplayRequest -> view.showScreen(instruction.screen)
                        is SoundDisplayRequest -> soundManager.playSound(instruction.soundResId)
                        is GameInputButtonDisplayRequest -> {
                            ledButtonsManager.showLed(instruction.gameInputButton, instruction.show)

                            if (instruction.show) {
                                soundManager.playSound(instruction.gameInputButton)
                            } else {
                                soundManager.stop()
                            }
                        }
                    }

                })

        gameEngine.start()
    }

    private fun buttonPressed(button: GameInputButton) {
        gameEngine.notifyButtonPressed(button)
    }

    fun release() {
        disposables.clear()
        ledButtonsManager.release()
    }

}
