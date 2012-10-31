package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.public.actions.Action

public trait ActiveObject: GameObject {
    public fun action(arena: Arena): Action
    public fun getInitPosition(arena: Arena): Pair<Int, Int>
}