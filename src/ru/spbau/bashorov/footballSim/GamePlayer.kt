package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.utils.*

private class GamePlayer(public val team: Team,
                     private val logic: Player,
                     public override val sym: Char,
                     private val invertCoordinates: Boolean): GameObject {

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
    public override fun getCellStatus(position: #(Int, Int)): CellStatus = arena.getCellStatus(position, player)
    public override fun getCoordinates(obj: Player): #(Int, Int) = arena.getCoordinates(obj)
    public override fun getBallCoordinates(): #(Int, Int) = arena.getBallCoordinates()
}

private class ArenaInvertCoordinatesWrapper(player: GamePlayer, arena: GameArena): ArenaWrapperAbstract(player, arena) {
    public override fun getCoordinates(obj: Player): #(Int, Int) = invertCoordinates(arena, arena.getCoordinates(obj))
    public override fun getCellStatus(position: #(Int, Int)): CellStatus = arena.getCellStatus(invertCoordinates(arena, position), player)
    public override fun getBallCoordinates(): #(Int, Int) = invertCoordinates(arena, arena.getBallCoordinates())
}