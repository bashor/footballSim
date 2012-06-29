package ru.spbau.bashorov.footballSim

public open class GameInternalException(message: String = "") : RuntimeException(message)

public class IllegalArgumentException(message: String = "") : GameInternalException(message)
public class BallNotFoundException(message: String = "Ball not found.") : GameInternalException(message)