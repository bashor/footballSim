package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.GameObject

public trait Arena {
    public val height: Int
    public val width: Int
    public val goalWidth: Int
    public fun getCoordinates(obj: PlayerBehavior): #(Int, Int)
    public fun getBallCoordinates(): #(Int, Int)
    public fun get(x: Int, y: Int): GameObject
    public fun get(position: #(Int,Int)): GameObject = get(position._1, position._2)
}