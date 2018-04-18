package fr.xebia.simonthings.gpio

import fr.xebia.simonthings.engine.GameInputButton

val CONTROLS = listOf(
        LedButton("BCM11", "BCM25", GameInputButton.YELLOW),
        LedButton("BCM16", "BCM12", GameInputButton.BLUE),
        LedButton("BCM26", "BCM19", GameInputButton.GREEN),
        LedButton("BCM6", "BCM20", GameInputButton.RED)
)