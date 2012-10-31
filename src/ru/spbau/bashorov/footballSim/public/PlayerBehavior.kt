package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.public.actions.Action

public trait PlayerBehavior {
    public fun action(position: Pair<Int, Int>, arena: Arena): Action
    public fun getInitPosition(arena: Arena): Pair<Int, Int>
}