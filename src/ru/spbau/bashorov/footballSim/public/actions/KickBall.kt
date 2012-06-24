package ru.spbau.bashorov.footballSim.public

public class KickBall(public val direction: Direction): Action {
    internal override fun invert(arena: ReadOnlyArena): Action = KickBall(direction.invert())
}