package fr.xebia.simonthings.engine

sealed class DisplayRequest
class ScreenDisplayRequest(val screen: Screen) : DisplayRequest()
class LedDisplayRequest(val gameInputButton: GameInputButton, val show: Boolean) : DisplayRequest()