package ru.spbau.bashorov.footballSim.utils

import java.util.Collections
import ru.spbau.bashorov.footballSim.public.Arena

fun invertCoordinates(arena: Arena, coordinates: Pair<Int, Int>) =
        Pair(arena.width - 1 - coordinates.first, arena.height - 1 - coordinates.second)

fun <T> MutableList<T>.shuffle() = Collections.shuffle(this)
