package ru.spbau.bashorov.footballSim.public.utils

import ru.spbau.bashorov.footballSim.public.ReadOnlyArena

public fun Tuple2<Int, Int>.minus(other: #(Int, Int)): Int =
    Math.max(Math.abs(this._1 - other._1), Math.abs(this._2 - other._2))

public fun Tuple2<Int, Int>.isApproachableFrom(from: #(Int, Int)): Boolean =
    (from - this) <= 1