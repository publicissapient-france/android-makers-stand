package fr.xebia.simonthings.engine

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.concurrent.timer

sealed class Instruction {}

class ScreenInstruction(val screen: Screen) : Instruction() {
}

class LedInstruction(val led: Button, val show: Boolean) : Instruction() {
}

enum class PlayState {
    LEARN,
    PLAY
}

enum class Screen {
    WAITING,
    PLAY,
    NAME
}

enum class Button {
    RED,
    GREEN,
    YELLOW,
    BLUE,
    WHITE
}

class Action {}


class GameEngine() {
    private var playState: PlayState = PlayState.LEARN
    private var screen: Screen = Screen.WAITING
    private val playerInputs = mutableListOf<Button>()

    private val instructions = mutableListOf<Button>()
    private val ps = PublishSubject.create<Instruction>()

    fun listen(): Observable<Instruction> {
        return ps
    }

    fun start() {
        ps.onNext(ScreenInstruction(Screen.WAITING))
    }

    private fun playSequence() {
        playerInputs.clear()
        instructions.add(Button.YELLOW)
        instructions.add(Button.WHITE)

        Observable.fromIterable(instructions)
                .concatMap { button ->
                    ps.onNext(LedInstruction(button, true))
                    return@concatMap Observable.timer(1, TimeUnit.SECONDS)
                            .map {
                                button
                            }
                }
                .map {
                    ps.onNext(LedInstruction(it, false))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    println("Done button $it")
                    playState = PlayState.PLAY
                }
    }

    fun buttonPressed(buttonPressed: Button) {

        println("Pressed ${buttonPressed.name}")

        when (screen) {
            Screen.WAITING -> {
                screen = Screen.PLAY
                playState = PlayState.LEARN
                ps.onNext(ScreenInstruction(Screen.PLAY))
                instructions.clear()
                playSequence()

                return
            }

            Screen.PLAY -> {

                if (playState == PlayState.LEARN) {
                    return
                }

                playerInputs.add(buttonPressed)
                playerInputs.forEachIndexed { i, btn ->
                    if (btn != instructions[i]) {
                        playState = PlayState.LEARN
                        screen = Screen.NAME
                        ps.onNext(ScreenInstruction(Screen.NAME))
                        return
                    }
                }

                if (playerInputs.size == instructions.size) {
                    playState = PlayState.LEARN
                }

                ps.onNext(LedInstruction(buttonPressed, true))
                Single.timer(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { _ ->
                            ps.onNext(LedInstruction(buttonPressed, false))

                            if (playerInputs.size == instructions.size) {
                                playSequence()
                            }
                        }

            }

            Screen.NAME -> {
                screen = Screen.PLAY
                playState = PlayState.LEARN
                ps.onNext(ScreenInstruction(Screen.PLAY))
                instructions.clear()
                playSequence()
            }
        }


    }
}