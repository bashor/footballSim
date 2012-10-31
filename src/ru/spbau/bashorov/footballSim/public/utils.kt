package ru.spbau.bashorov.footballSim.public.utils

import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.Direction
import ru.spbau.bashorov.footballSim.public.exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.gameObjects.Free

public fun Pair<Int, Int>.calcDistanceTo(other: Pair<Int, Int>): Int =
    Math.max(Math.abs(this.first - other.first), Math.abs(this.second - other.second))

public fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
        Pair(this.first + other.first, this.second + other.second)

public fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> =
        Pair(this.first - other.first, this.second - other.second)

public fun Pair<Int, Int>.equals(other: Pair<Int, Int>): Boolean =
    this.first == other.first && this.second == other.second

public fun Pair<Int, Int>.isAchievableFrom(from: Pair<Int, Int>): Boolean =
    from.calcDistanceTo(this) <= 1

public fun Pair<Int, Int>.isValidCellOn(arena: Arena): Boolean =
    this.first >= 0 && this.first < arena.width && this.second >= 0 && this.second < arena.height

fun stepTo(from: Pair<Int, Int>, to: Pair<Int, Int>, arena: Arena): Pair<Int, Int> {
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
            .sort(comparator<Pair<Int, Int>> { (a, b) -> (to.calcDistanceTo(a)).compareTo(to.calcDistanceTo(b)) });

    for (i in achievable) {
        if (arena[i] is Free) {
            return i
        }
    }

    throw AchievablePositionNotFoundException()
}