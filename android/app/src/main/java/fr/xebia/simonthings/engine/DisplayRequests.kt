package fr.xebia.simonthings.engine

sealed class DisplayRequest
class ScreenDisplayRequest(val screen: Screen) : DisplayRequest()
class ScoreDisplayRequest(val score: Int) : DisplayRequest()
class GameInputButtonDisplayRequest(val gameInputButton: GameInputButton, val show: Boolean) : DisplayRequest()
class SoundDisplayRequest(val soundResId: Int) : DisplayRequest()
class PlayerUpdateRequest(val player: Player) : DisplayRequest()
class EndGamePersistRequest(val player: Player): DisplayRequest()
