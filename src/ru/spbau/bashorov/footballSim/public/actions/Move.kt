package ru.spbau.bashorov.footballSim.public.actions

import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.utils.invertCoordinates

public class Move(val position: Pair<Int, Int>): Action {
    internal override fun invert(arena: Arena): Action = Move(invertCoordinates(arena, position))
}