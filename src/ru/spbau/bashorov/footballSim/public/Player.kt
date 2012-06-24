package ru.spbau.bashorov.footballSim.public

public trait Player {
    public fun action(position: #(Int, Int), arena: Arena): Action
    public fun getInitPosition(arena: Arena): #(Int, Int)
}