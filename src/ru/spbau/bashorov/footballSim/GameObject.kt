package ru.spbau.bashorov.footballSim

public trait GameObject {
    public fun action(arena: Arena) : Action
    public val sym : Char
}