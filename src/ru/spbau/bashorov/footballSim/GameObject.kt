package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Action

trait GameObject {
    public fun action(arena: Arena): Action
    public fun getInitPosition(arena: Arena): #(Int, Int)
    public val sym: Char
}