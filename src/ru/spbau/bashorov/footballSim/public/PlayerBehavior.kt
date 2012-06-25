package ru.spbau.bashorov.footballSim.public

public trait PlayerBehavior {
    public fun action(position: #(Int, Int), arena: Arena): Action
    public fun getInitPosition(arena: Arena): #(Int, Int)
}