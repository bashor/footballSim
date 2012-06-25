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
    public override fun action(arena: GameArena): Action =
        playerBehavior.action(invertCoordinates(arena, arena.getCoordinates(this)), ArenaInvertCoordinatesWrapper(this, arena))

    public override fun getInitPosition(arena: GameArena): #(Int, Int) =
        invertCoordinates(arena, playerBehavior.getInitPosition(ArenaInvertCoordinatesWrapper(this, arena)))
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