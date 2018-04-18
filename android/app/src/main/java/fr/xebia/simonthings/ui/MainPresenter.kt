package fr.xebia.simonthings.ui

import com.google.android.things.pio.PeripheralManager
import fr.xebia.simonthings.engine.*
import fr.xebia.simonthings.gpio.LedButtonManager
import fr.xebia.simonthings.persistance.FirebasePersister
import fr.xebia.simonthings.ui.SoundManager.Companion.NOTE_G5
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(private val view: MainActivity, private val soundManager: SoundManager) {

    private val gameEngine = GameEngine()
    private val ledButtonsManager = LedButtonManager()
    private val disposables = CompositeDisposable()

    fun init() {
        ledButtonsManager.init(PeripheralManager.getInstance(), this::buttonPressed)
        soundManager.init()

        disposables.add(gameEngine.listen()
                .subscribe { instruction ->
                    when (instruction) {
                        is PlayerUpdateRequest -> FirebasePersister.persistPlayer(instruction.player)
                        is EndGamePersistRequest -> {
                            FirebasePersister.addScore(instruction.player)
                            FirebasePersister.clearPlayer()
                        }
                        is ScreenDisplayRequest -> view.showScreen(instruction.screen)
                        is SoundDisplayRequest -> soundManager.playSound(instruction.soundResId, 0.0)
                        is ScoreDisplayRequest -> {
                            view.showScore(instruction.score)
                        }
                        is GameInputButtonDisplayRequest -> {
                            view.showLedPress(instruction.gameInputButton, instruction.show)

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
        soundManager.release()
    }

    fun onNameEntered(name: String) {
        gameEngine.startNewGame(name)
    }
}
