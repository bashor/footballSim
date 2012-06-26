package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.GameObject
import ru.spbau.bashorov.footballSim.public.gameObjects.*
import ru.spbau.bashorov.footballSim.utils.invertCoordinates
import ru.spbau.bashorov.footballSim.public.exceptions.UnknownObjectException

private open class ArenaWrapper(protected final val player: GamePlayer, protected final val arena: Arena): Arena {
    public final override val height: Int = arena.height
    public final override val width: Int = arena.width
    public final override val goalWidth: Int = arena.goalWidth

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
            else -> throw UnknownObjectException()
        }
    }
}

private class ArenaInvertCoordinatesWrapper(player: GamePlayer, arena: Arena): ArenaWrapper(player, arena) {
    public override fun get(x: Int, y: Int): GameObject {
        val c = invertCoordinates(arena, #(x, y))
        return super.get(c._1, c._2)
    }
}