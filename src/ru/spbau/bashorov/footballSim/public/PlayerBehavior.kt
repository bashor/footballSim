package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.public.actions.Action

public trait PlayerBehavior {
    public fun action(position: #(Int, Int), arena: Arena): Action
    public fun getInitPosition(arena: Arena): #(Int, Int)
}