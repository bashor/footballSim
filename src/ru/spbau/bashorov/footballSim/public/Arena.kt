package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.Ball
import ru.spbau.bashorov.footballSim.BallNotFoundException
import ru.spbau.bashorov.footballSim.public.exceptions.ObjectNotFoundException
import ru.spbau.bashorov.footballSim.public.gameObjects.PartnerPlayer
import ru.spbau.bashorov.footballSim.public.gameObjects.ReadOnlyBall

public trait Arena {
    public val height: Int
    public val width: Int
    public val goalWidth: Int
    public fun get(x: Int, y: Int): GameObject
    public fun get(position: #(Int,Int)): GameObject = get(position._1, position._2)
}

// Arena utility functions

inline public fun Arena.getCoordinates(obj: PlayerBehavior): #(Int, Int) =
    getCoordinates({it is PartnerPlayer && it.playerBehavior == obj})

inline public fun Arena.getCoordinates(obj: GameObject): #(Int, Int) =
    getCoordinates({it == obj})

inline public fun Arena.getBallCoordinates(): #(Int, Int) {
    try {
        return getCoordinates({it is ReadOnlyBall || it is Ball})
    } catch (e: ObjectNotFoundException) {
        throw BallNotFoundException()
    }
}

inline public fun Arena.getCoordinates(predicate: (GameObject)->Boolean): #(Int, Int) {
    for (i in 0..width - 1) {
        for (j in 0..height - 1) {
            if (predicate(this[i,j]))
                return #(i, j)
        }
    }
    throw ObjectNotFoundException()
}