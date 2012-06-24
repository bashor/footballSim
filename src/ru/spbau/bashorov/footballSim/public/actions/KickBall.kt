package ru.spbau.bashorov.footballSim.public

public class KickBall(public val direction: Direction): Action {
    internal override fun invert(arena: Arena): Action = KickBall(direction.invert())
}