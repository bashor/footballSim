package ru.spbau.bashorov.footballSim.public

public trait Arena {
    public val height: Int
    public val width: Int
    public val goalWidth: Int
    public fun getCoordinates(obj: Player): #(Int, Int)
    public fun getBallCoordinates(): #(Int, Int)
    public fun getCellStatus(position: #(Int, Int)): CellStatus
}