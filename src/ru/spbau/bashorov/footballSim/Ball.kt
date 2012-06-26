package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.actions.*
import ru.spbau.bashorov.footballSim.public.gameObjects.Free
import ru.spbau.bashorov.footballSim.public.utils.*

private class Ball: ActiveObject {
    public override val sym: Char = '\u25CF' // ●
    private var direction: Direction = Direction.NOWHERE

    public override fun action(arena: Arena): Action {
        if (direction == Direction.NOWHERE)
            return DoNothing();

        var newPosition = arena.getCoordinates(this) + direction.shift

        direction = Direction.NOWHERE
        if (newPosition isValidCellOn arena && arena[newPosition] !is Free) {
            return DoNothing()
        }

        return Move(newPosition)
    }

    public override fun getInitPosition(arena: Arena): #(Int, Int) =
        #(arena.width / 2, arena.height / 2)

    public fun kick(kickAction: KickBall) {
        direction = kickAction.direction
    }
}