package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.invertCoordinates

public class Move(val position: #(Int, Int)): Action {
    internal override fun invert(arena: ReadOnlyArena): Action =
        Move(invertCoordinates(arena, position))
}