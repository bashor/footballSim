package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.actions.Action
import ru.spbau.bashorov.footballSim.utils.*

private open class GamePlayer(public val team: Team,
                               public val playerBehavior: PlayerBehavior,
                               public override val sym: Char): ActiveObject {
    public override fun action(arena: Arena): Action =
        playerBehavior.action(arena.getCoordinates(this), ArenaWrapper(this, arena))

    public override fun getInitPosition(arena: Arena): Pair<Int, Int> =
        playerBehavior.getInitPosition(ArenaWrapper(this, arena))

    public fun equals(obj: Any?): Boolean = this === obj || playerBehavior === obj
}

private class GamePlayerInvertCoordinates(team: Team, playerBehavior: PlayerBehavior, sym: Char): GamePlayer(team, playerBehavior, sym) {
    public override fun action(arena: Arena): Action {
        val readOnlyArena = ArenaInvertCoordinatesWrapper(this, arena)
        val position = invertCoordinates(readOnlyArena, arena.getCoordinates(this))
        val action = playerBehavior.action(position, readOnlyArena)
        return action.invert(readOnlyArena)
    }

    public override fun getInitPosition(arena: Arena): Pair<Int, Int> =
        invertCoordinates(arena, playerBehavior.getInitPosition(ArenaInvertCoordinatesWrapper(this, arena)))
}