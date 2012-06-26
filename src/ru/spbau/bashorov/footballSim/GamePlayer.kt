package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.utils.*

private abstract class GamePlayer(public val team: Team,
                         protected val playerBehavior: PlayerBehavior,
                         public override val sym: Char): ActiveObject {

    public fun equals(obj: Any?): Boolean = this === obj || playerBehavior === obj
}

private class GamePlayerNormal(team: Team, playerBehavior: PlayerBehavior, sym: Char): GamePlayer(team, playerBehavior, sym) {
    public override fun action(arena: GameArena): Action =
        playerBehavior.action(arena.getCoordinates(this), ArenaWrapper(this, arena))

    public override fun getInitPosition(arena: GameArena): #(Int, Int) =
        playerBehavior.getInitPosition(ArenaWrapper(this, arena))
}

private class GamePlayerInvertCoordinates(team: Team, playerBehavior: PlayerBehavior, sym: Char): GamePlayer(team, playerBehavior, sym) {
    public override fun action(arena: GameArena): Action {
        val readOnlyArena = ArenaInvertCoordinatesWrapper(this, arena)
        val position = invertCoordinates(readOnlyArena, arena.getCoordinates(this))
        return playerBehavior.action(position, readOnlyArena).invert(readOnlyArena)
    }

    public override fun getInitPosition(arena: GameArena): #(Int, Int) =
        invertCoordinates(arena, playerBehavior.getInitPosition(ArenaInvertCoordinatesWrapper(this, arena)))
}

private open class ArenaWrapper(protected final val player: GamePlayer, protected final val arena: GameArena): Arena {
    public final override val height: Int = arena.height
    public final override val width: Int = arena.width
    public final override val goalWidth: Int = arena.goalWidth

    public override fun getCoordinates(obj: PlayerBehavior): #(Int, Int) = arena.getCoordinates(obj)
    public override fun getBallCoordinates(): #(Int, Int) = arena.getBallCoordinates()

    public override fun get(x: Int, y: Int): GameObject {
        val obj = arena[x, y]
        return when (obj) {
            is GamePlayer-> {
                if (obj.team == player.team) {
                    PartnerPlayer(obj)
                } else {
                    OpponentPlayer(obj)
                }
            }
            is Ball -> ReadOnlyBall(obj)
            is Free -> obj
            else -> throw Exception()
        }
    }
}

private class ArenaInvertCoordinatesWrapper(player: GamePlayer, arena: GameArena): ArenaWrapper(player, arena) {
    public override fun getCoordinates(obj: PlayerBehavior): #(Int, Int) = invertCoordinates(arena, super.getCoordinates(obj))
    public override fun getBallCoordinates(): #(Int, Int) = invertCoordinates(arena, super.getBallCoordinates())
    public override fun get(x: Int, y: Int): GameObject {
        val c = invertCoordinates(arena, #(x, y))
        return super.get(c._1, c._2)
    }
}