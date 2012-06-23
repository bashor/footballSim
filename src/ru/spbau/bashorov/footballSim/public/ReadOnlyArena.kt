package ru.spbau.bashorov.footballSim.public

public trait ReadOnlyArena {
    public val height: Int
    public val width: Int
    public val goalWidth: Int
    public fun getCoordinates(obj: PlayerLogic): #(Int, Int)
    public fun getBallCoordinates(): #(Int, Int)
    public fun cellIsFree(position: #(Int, Int)): Boolean
}