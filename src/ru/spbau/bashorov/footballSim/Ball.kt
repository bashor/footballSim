package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*

// KT static не работает :(
//private //KT не видно внутри функции

class Ball: GameObject {
    private class object {
        private val DURATION_TO_COORD_OFFSET = hashMap(
                Direction.NORTH     to #(+1,  0),
                Direction.SOUTH     to #(-1,  0),
                Direction.WEST      to #( 0, +1),
                Direction.EAST      to #( 0, -1),
                Direction.NORTHWEST to #(+1, -1),
                Direction.NORTHEAST to #(+1, +1),
                Direction.SOUTHWEST to #(-1, -1),
                Direction.SOUTHEAST to #(-1, +1))
    }
    public override fun action(arena: Arena): Action {
        if (direction == Direction.NOWHERE)
            return Nothing();

        val curPosition = arena.getCoordinates(this)

        val coordOffset = DURATION_TO_COORD_OFFSET[direction]

        if (coordOffset == null) {
            throw Exception("beda")
        }

        var newPosition = #(curPosition._1 + coordOffset._1, curPosition._2 + coordOffset._2)

        if (newPosition._1 < 0)
            newPosition = #(0, newPosition._2)

        if (newPosition._2 < 0)
            newPosition = #(newPosition._1, 0)

        if (newPosition._1 >= arena.width)
            newPosition = #(arena.width - 1, newPosition._2)

        if (newPosition._2 >= arena.height)
            newPosition = #(newPosition._1, arena.height - 1)

        direction = Direction.NOWHERE

        if (!arena.cellIsFree(newPosition)) {
            return Nothing()
        }

        return Move(newPosition)
    }

    public override fun getInitPosition(arena: Arena): #(Int, Int) =
        #(arena.width / 2, arena.height / 2)

    public fun kick(kickAction: KickBall) {
        direction = kickAction.direction
    }

    private var direction: Direction = Direction.NOWHERE
    // KT без public доступ обнаруживается только в run тайме
    public override val sym: Char = '\u25CF' // '\u25C9'
}