package ru.spbau.bashorov.footballSim.utils

import ru.spbau.bashorov.footballSim.public.ReadOnlyArena
import java.util.Collection
import java.util.ArrayList
import java.util.Comparator
import java.util.Collections
import java.util.List

fun invertCoordinates(arena: ReadOnlyArena, coordinates: #(Int, Int)) =
    #(arena.width - 1 - coordinates._1, arena.height - 1 - coordinates._2)

fun <T> ArrayList<T>.sort(comparator: (T,T)->Int): Collection<T> =
    this.sort(object: Comparator<T>{
        public override fun compare(o1: T?, o2: T?): Int = comparator(o1!!, o2!!)
        public override fun equals(obj: Any?): Boolean = this === obj
    })

fun <T> List<T>.shuffle() {
    Collections.shuffle(this)
}