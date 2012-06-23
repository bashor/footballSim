package ru.spbau.bashorov.footballSim.public

public trait PlayerLogic {
    public fun action(position: #(Int, Int), arena: ReadOnlyArena): Action
    public fun getInitPosition(arena: ReadOnlyArena): #(Int, Int)
}