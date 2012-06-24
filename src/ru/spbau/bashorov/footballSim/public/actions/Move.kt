package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.utils.invertCoordinates

public class Move(val position: #(Int, Int)): Action {
    internal override fun invert(arena: Arena): Action = Move(invertCoordinates(arena, position))
}