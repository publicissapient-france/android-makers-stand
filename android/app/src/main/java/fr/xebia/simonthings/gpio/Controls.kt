package fr.xebia.simonthings.gpio

import fr.xebia.simonthings.engine.GameInputButton

val CONTROLS = listOf(
        LedButton("BCM26", "BCM21", GameInputButton.YELLOW),
        LedButton("BCM20", "BCM16", GameInputButton.GREEN),
        LedButton("BCM7", "BCM8", GameInputButton.BLUE),
        LedButton("BCM24", "BCM23", GameInputButton.RED)
)