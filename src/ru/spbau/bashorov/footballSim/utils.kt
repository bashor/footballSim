package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.ReadOnlyArena

fun invertCoordinates(arena: ReadOnlyArena, coordinates: #(Int, Int)) =
    #(arena.width - 1 - coordinates._1, arena.height - 1 - coordinates._2)
