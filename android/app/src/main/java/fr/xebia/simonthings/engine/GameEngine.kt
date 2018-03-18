package fr.xebia.simonthings.engine

import fr.xebia.simonthings.R
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class GameEngine {
    private var playState: PlayState = PlayState.LEARN
    private var screen: Screen = Screen.WAITING

    private val playerInputs = mutableListOf<GameInputButton>()
    private val instructions = mutableListOf<GameInputButton>()

    private val ps = PublishSubject.create<DisplayRequest>()

    fun listen(): Observable<DisplayRequest> {
        return ps
    }

    fun start() {
        ps.onNext(ScreenDisplayRequest(Screen.WAITING))
    }

    private fun showSequence() {
        playerInputs.clear()
        instructions.add(GameInputButton.YELLOW)
        instructions.add(GameInputButton.WHITE)

        playState = PlayState.LEARN

        Observable.fromIterable(instructions)
                .concatMap { button ->
                    ps.onNext(GameInputButtonDisplayRequest(button, true))
                    return@concatMap Observable.timer(1, TimeUnit.SECONDS)
                            .map {
                                button
                            }
                }
                .map {
                    ps.onNext(GameInputButtonDisplayRequest(it, false))
                }
                .doOnComplete {
                    playState = PlayState.PLAY
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                }
    }

    fun notifyButtonPressed(gameInputButtonPressed: GameInputButton) {
        println("DEBUG INPUT  ${gameInputButtonPressed.name}")

        when (screen) {
            Screen.NAME -> startNewGame()
            Screen.WAITING -> startNewGame()
            Screen.PLAY -> handlePlayInput(gameInputButtonPressed)
        }
    }

    private fun handlePlayInput(gameInputButtonPressed: GameInputButton) {
        if (playState == PlayState.LEARN) {
            println("DEBUG PLAY INPUT IGNORED ${gameInputButtonPressed.name}")
            return
        }

        println("DEBUG PLAY INPUT ${gameInputButtonPressed.name}")

        playerInputs.add(gameInputButtonPressed)

        playerInputs.forEachIndexed { i, btn ->
            if (btn != instructions[i]) {
                playState = PlayState.LEARN
                screen = Screen.NAME
                ps.onNext(ScreenDisplayRequest(Screen.NAME))
                ps.onNext(SoundDisplayRequest(R.raw.end))

                println("DEBUG END OF GAME")
                return
            }
        }

        if (playerInputs.size == instructions.size) {
            println("DEBUG STOP PLAYING AND LEARN")
            playState = PlayState.LEARN
        }

        ps.onNext(GameInputButtonDisplayRequest(gameInputButtonPressed, true))
        Single.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { _ ->
                    ps.onNext(GameInputButtonDisplayRequest(gameInputButtonPressed, false))

                    if (playerInputs.size == instructions.size) {
                        println("DEBUG HE WON")

                        ps.onNext(SoundDisplayRequest(R.raw.start))
                        Single.timer(1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe { _ ->
                                    showSequence()
                                }
                    }
                }
    }

    private fun startNewGame() {
        instructions.clear()
        screen = Screen.PLAY
        playState = PlayState.LEARN

        ps.onNext(ScreenDisplayRequest(Screen.PLAY))
        ps.onNext(SoundDisplayRequest(R.raw.start))

        Single.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { _ ->
                    showSequence()
                }
    }
}