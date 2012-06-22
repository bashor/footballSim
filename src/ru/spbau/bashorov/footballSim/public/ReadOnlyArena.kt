package ru.spbau.bashorov.footballSim.public

public trait ReadOnlyArena {
    public fun getCoordinates(obj: PlayerLogic): #(Int, Int)
    public fun getBallCoordinates(): #(Int, Int)
    public fun cellIsFree(position: #(Int, Int)): Boolean
}