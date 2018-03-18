package fr.xebia.simonthings.engine

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

    private fun playSequence() {
        playerInputs.clear()
        instructions.add(GameInputButton.YELLOW)
        instructions.add(GameInputButton.WHITE)

        playState = PlayState.LEARN

        Observable.fromIterable(instructions)
                .concatMap { button ->
                    ps.onNext(LedDisplayRequest(button, true))
                    return@concatMap Observable.timer(1, TimeUnit.SECONDS)
                            .map {
                                button
                            }
                }
                .map {
                    ps.onNext(LedDisplayRequest(it, false))
                }
                .doOnComplete {
                    playState = PlayState.PLAY
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                }
    }

    fun buttonPressed(gameInputButtonPressed: GameInputButton) {

        println("Pressed ${gameInputButtonPressed.name} state is ${playState.name}")

        when (screen) {
            Screen.WAITING -> {
                screen = Screen.PLAY
                playState = PlayState.LEARN
                ps.onNext(ScreenDisplayRequest(Screen.PLAY))
                instructions.clear()
                playSequence()

                return
            }

            Screen.PLAY -> {

                if (playState == PlayState.LEARN) {
                    return
                }

                playerInputs.add(gameInputButtonPressed)
                playerInputs.forEachIndexed { i, btn ->
                    if (btn != instructions[i]) {
                        playState = PlayState.LEARN
                        screen = Screen.NAME
                        ps.onNext(ScreenDisplayRequest(Screen.NAME))
                        return
                    }
                }

                if (playerInputs.size == instructions.size) {
                    playState = PlayState.LEARN
                }

                ps.onNext(LedDisplayRequest(gameInputButtonPressed, true))
                Single.timer(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { _ ->
                            ps.onNext(LedDisplayRequest(gameInputButtonPressed, false))

                            if (playerInputs.size == instructions.size) {
                                playSequence()
                            }
                        }

            }

            Screen.NAME -> {
                screen = Screen.PLAY
                playState = PlayState.LEARN
                ps.onNext(ScreenDisplayRequest(Screen.PLAY))
                instructions.clear()
                playSequence()
            }
        }


    }
}