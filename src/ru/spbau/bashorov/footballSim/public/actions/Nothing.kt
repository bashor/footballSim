package ru.spbau.bashorov.footballSim.public

public class Nothing: Action {
    internal override fun invert(arena: ReadOnlyArena): Action = this
}