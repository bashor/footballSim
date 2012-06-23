package ru.spbau.bashorov.footballSim.public

public trait Action {
    internal fun invert(arena: ReadOnlyArena): Action
}