package ru.spbau.bashorov.footballSim.public

public trait PlayerLogic {
    fun action(position: #(Int, Int), arena: ReadOnlyArena) : Action
    val initPosition: #(Int, Int)
}