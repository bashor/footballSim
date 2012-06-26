package ru.spbau.bashorov.footballSim.public.utils

import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.Direction
import ru.spbau.bashorov.footballSim.public.exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.gameObjects.Free
import ru.spbau.bashorov.footballSim.utils.sort

public fun Tuple2<Int, Int>.calcDistanceTo(other: #(Int, Int)): Int =
    Math.max(Math.abs(this._1 - other._1), Math.abs(this._2 - other._2))

public fun Tuple2<Int, Int>.plus(other: #(Int, Int)): #(Int, Int) =
    #(this._1 + other._1, this._2 + other._2)

public fun Tuple2<Int, Int>.minus(other: #(Int, Int)): #(Int, Int) =
    #(this._1 - other._1, this._2 - other._2)

public fun Tuple2<Int, Int>.equals(other: #(Int, Int)): Boolean =
    this._1 == other._1 && this._2 == other._2

public fun Tuple2<Int, Int>.isAchievableFrom(from: #(Int, Int)): Boolean =
    from.calcDistanceTo(this) <= 1

public fun Tuple2<Int, Int>.isValidCellOn(arena: Arena): Boolean =
    this._1 >= 0 && this._1 < arena.width && this._2 >= 0 && this._2 < arena.height

fun stepTo(from: #(Int, Int), to: #(Int, Int), arena: Arena): #(Int, Int) {
    val achievable = arrayList(
            from + Direction.FORWARD.shift,
            from + Direction.BACKWARD.shift,
            from + Direction.LEFT.shift,
            from + Direction.RIGHT.shift,
            from + Direction.FORWARD_LEFT.shift,
            from + Direction.FORWARD_RIGHT.shift,
            from + Direction.BACKWARD_LEFT.shift,
            from + Direction.BACKWARD_RIGHT.shift)
            .filter{it isValidCellOn arena}
            .sort{a, b -> (to.calcDistanceTo(a)).compareTo(to.calcDistanceTo(b))};

    for (i in achievable) {
        if (arena[i] is Free) {
            return i
        }
    }

    throw AchievablePositionNotFoundException()
}