package fr.xebia.simonthings.engine

import fr.xebia.simonthings.R
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

data class Player(val id: String, val name: String, var count: Int, var start: Long, var time: Long, var email: String)

class GameEngine {
    private var player: Player? = null
    private var playState: PlayState = PlayState.LEARN
    private var screen: Screen = Screen.NAME

    private val playerInputs = mutableListOf<GameInputButton>()
    private val instructions = mutableListOf<GameInputButton>()

    private val ps = PublishSubject.create<DisplayRequest>()

    fun listen(): Observable<DisplayRequest> {
        return ps
    }

    fun start() {
        ps.onNext(ScreenDisplayRequest(Screen.NAME))
    }

    private fun showSequence() {
        playerInputs.clear()

        instructions.add(getRandomColor())

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

    private fun getRandomColor(): GameInputButton = GameInputButton.values()[Random().nextInt(GameInputButton.values().size)]

    fun notifyButtonPressed(gameInputButtonPressed: GameInputButton) {
        println("DEBUG INPUT  ${gameInputButtonPressed.name}")

        when (screen) {
            Screen.NAME -> {
                println("Ignored")
            }
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

                player?.let {
                    it.time = (System.currentTimeMillis() - it.start).toInt().toLong() / 1000 * 1000
                    ps.onNext(EndGamePersistRequest(it))
                }
                println("DEBUG END OF GAME")
                return
            }
        }

        var won = false
        if (playerInputs.size == instructions.size) {
            won = true
            println("DEBUG STOP PLAYING AND LEARN")
            playState = PlayState.LEARN
        }

        ps.onNext(GameInputButtonDisplayRequest(gameInputButtonPressed, true))
        Single.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    ps.onNext(GameInputButtonDisplayRequest(gameInputButtonPressed, false))

                    if (won) {
                        ps.onNext(ScoreDisplayRequest(instructions.size))
                        println("DEBUG HE WON")

                        player?.let {
                            it.count++
                            ps.onNext(PlayerUpdateRequest(it))
                        }

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
    val emailMatcher = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z0-9]")
    fun startNewGame(name: String) {
        val matcher = emailMatcher.matcher(name)
        if (!matcher.find()) {
            return
        }

        val email = matcher.group(0)
        val filteredName = name.replace(email, "").trim()

        if (email.isNullOrEmpty() || filteredName.isNullOrEmpty()) {
            return
        }

        player = Player(UUID.randomUUID().toString(), filteredName, 0, System.currentTimeMillis(), 0, email.trim())
        instructions.clear()
        screen = Screen.PLAY
        playState = PlayState.LEARN

        ps.onNext(ScreenDisplayRequest(Screen.PLAY))
        ps.onNext(SoundDisplayRequest(R.raw.start))

        player?.let {
            ps.onNext(PlayerUpdateRequest(it))
        }

        Single.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { _ ->
                    showSequence()
                }
    }
}