package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.utils.*

private class GamePlayer(public val team: Team,
                     private val logic: PlayerBehavior,
                     public override val sym: Char,
                     private val invertCoordinates: Boolean): ActiveObject {

    public override fun action(arena: GameArena): Action {
        var position = arena.getCoordinates(this)
        var readOnlyArena: Arena?
        if (invertCoordinates) {
            position = invertCoordinates(arena, position)
            readOnlyArena = ArenaInvertCoordinatesWrapper(this, arena)
        }
        else {
            readOnlyArena = ArenaWrapper(this, arena)
        }

        val action = logic.action(position, readOnlyArena!!)

        return if (invertCoordinates) action.invert(readOnlyArena!!) else action
    }

    public override fun getInitPosition(arena: GameArena): #(Int, Int) =
        if (invertCoordinates)
            invertCoordinates(arena, logic.getInitPosition(ArenaInvertCoordinatesWrapper(this, arena)))
        else
            logic.getInitPosition(ArenaWrapper(this, arena))

    public fun equals(obj: Any?): Boolean = this === obj || logic === obj
}

private abstract class ArenaWrapperAbstract(protected final val player: GamePlayer, protected final val arena: GameArena): Arena {
    public final override val height: Int = arena.height
    public final override val width: Int = arena.width
    public final override val goalWidth: Int = arena.goalWidth
}

private class ArenaWrapper(player: GamePlayer, arena: GameArena): ArenaWrapperAbstract(player, arena) {
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
    public override fun getCoordinates(obj: PlayerBehavior): #(Int, Int) = arena.getCoordinates(obj)
    public override fun getBallCoordinates(): #(Int, Int) = arena.getBallCoordinates()
}

private class ArenaInvertCoordinatesWrapper(player: GamePlayer, arena: GameArena): ArenaWrapperAbstract(player, arena) {
    public override fun get(x: Int, y: Int): GameObject {
        val c = invertCoordinates(arena, #(x, y))
        return arena[c._1, c._2]
    }
    public override fun getCoordinates(obj: PlayerBehavior): #(Int, Int) = invertCoordinates(arena, arena.getCoordinates(obj))
    public override fun getBallCoordinates(): #(Int, Int) = invertCoordinates(arena, arena.getBallCoordinates())
}