package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Action

public trait GameObject {
    public fun action(arena: Arena) : Action
    public val sym : Char
}