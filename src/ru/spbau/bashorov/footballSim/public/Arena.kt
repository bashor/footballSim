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
    public fun get(position: Pair<Int, Int>): GameObject = get(position.first, position.second)
}

// Arena utility functions

inline public fun Arena.getCoordinates(obj: PlayerBehavior): Pair<Int, Int> =
    getCoordinates({it is PartnerPlayer && it.playerBehavior == obj})

inline public fun Arena.getCoordinates(obj: GameObject): Pair<Int, Int> =
    getCoordinates({it == obj})

inline public fun Arena.getBallCoordinates(): Pair<Int, Int> {
    try {
        return getCoordinates({it is ReadOnlyBall || it is Ball})
    } catch (e: ObjectNotFoundException) {
        throw BallNotFoundException()
    }
}

inline public fun Arena.getCoordinates(predicate: (GameObject)->Boolean): Pair<Int, Int> {
    for (i in 0..width - 1) {
        for (j in 0..height - 1) {
            if (predicate(this[i,j]))
                return Pair(i, j)
        }
    }
    throw ObjectNotFoundException()
}