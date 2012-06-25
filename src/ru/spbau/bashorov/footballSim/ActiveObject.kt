package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Action

private trait ActiveObject: GameObject {
    public fun action(arena: GameArena): Action
    public fun getInitPosition(arena: GameArena): #(Int, Int)
}