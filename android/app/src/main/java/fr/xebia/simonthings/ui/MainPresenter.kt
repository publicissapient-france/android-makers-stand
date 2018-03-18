package fr.xebia.simonthings.ui

import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.GameEngine
import fr.xebia.simonthings.engine.GameInputButton
import fr.xebia.simonthings.engine.LedDisplayRequest
import fr.xebia.simonthings.engine.ScreenDisplayRequest
import fr.xebia.simonthings.gpio.LedButtonManager
import io.reactivex.disposables.CompositeDisposable


class MainPresenter(private val view: MainActivity) {

    private val gameEngine = GameEngine()
    private val ledButtonsManager = LedButtonManager()
    private val disposables = CompositeDisposable()

    fun init() {
        ledButtonsManager.init(PeripheralManager.getInstance(), this::buttonPressed)

        disposables.add(gameEngine.listen()
                .subscribe { instruction ->
                    when (instruction) {
                        is ScreenDisplayRequest -> view.showScreen(instruction.screen)
                        is LedDisplayRequest -> ledButtonsManager.showLed(instruction.gameInputButton, instruction.show)
                    }

                })

        gameEngine.start()
    }

    private fun buttonPressed(button: GameInputButton) {
        gameEngine.buttonPressed(button)
    }

    fun release() {
        disposables.clear()
        ledButtonsManager.release()
    }

}
