package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Action

private trait GameObject {
    public fun action(arena: GameArena): Action
    public fun getInitPosition(arena: GameArena): #(Int, Int)
    public val sym: Char
}