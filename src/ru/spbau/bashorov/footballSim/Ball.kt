package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*

private class Ball: ActiveObject {
    public override val sym: Char = '\u25CF' // ●
    private var direction: Direction = Direction.NOWHERE

    public override fun action(arena: GameArena): Action {
        if (direction == Direction.NOWHERE)
            return Nothing();

        var newPosition = arena.getCoordinates(this) + direction.shift

        direction = Direction.NOWHERE
        val st = arena.getCellStatus(newPosition)
        if (st != CellStatus.UNACHIEVABLE && st == CellStatus.OCCUPIED) {
            return Nothing()
        }

        return Move(newPosition)
    }

    public override fun getInitPosition(arena: GameArena): #(Int, Int) =
        #(arena.width / 2, arena.height / 2)

    public fun kick(kickAction: KickBall) {
        direction = kickAction.direction
    }
}