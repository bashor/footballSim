package ru.spbau.bashorov.footballSim.public.utils

import ru.spbau.bashorov.footballSim.public.ReadOnlyArena
import java.util.ArrayList
import ru.spbau.bashorov.footballSim.utils.sort
import ru.spbau.bashorov.footballSim.public.Direction

public fun Tuple2<Int, Int>.minus(other: #(Int, Int)): Int =
    Math.max(Math.abs(this._1 - other._1), Math.abs(this._2 - other._2))

public fun Tuple2<Int, Int>.plus(other: #(Int, Int)): #(Int, Int) =
    #(this._1 + other._1, this._2 + other._2)

public fun Tuple2<Int, Int>.isApproachableFrom(from: #(Int, Int)): Boolean =
    (from - this) <= 1

fun stepTo(from: #(Int, Int), to: #(Int, Int), arena: ReadOnlyArena): #(Int, Int) {
    val approachable = arrayList(
            from + Direction.FORWARD.shift,
            from + Direction.BACKWARD.shift,
            from + Direction.LEFT.shift,
            from + Direction.RIGHT.shift,
            from + Direction.FORWARDLEFT.shift,
            from + Direction.FORWARDRIGHT.shift,
            from + Direction.BACKWARDLEFT.shift,
            from + Direction.BACKWARDRIGHT.shift)

    approachable.sort({a, b -> (to - a).compareTo(to - b)});
    for (i in approachable) {
        try {
            if (arena.cellIsFree(i)) {
                return i
            }
        } catch(e: Exception) {}
    }
    throw Exception()
}