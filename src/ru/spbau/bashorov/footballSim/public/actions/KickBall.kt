package ru.spbau.bashorov.footballSim.public.actions

import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.Direction

public class KickBall(public val direction: Direction): Action {
    internal override fun invert(arena: Arena): Action = KickBall(direction.invert())
}