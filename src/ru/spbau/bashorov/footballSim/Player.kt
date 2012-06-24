package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.utils.*

private class Player(public val team: Team, val logic: PlayerLogic, override val sym: Char, private val invertCoordinates: Boolean): GameObject {
    private class InvertCoordinatesArena(private val player: Player, private val arena: Arena): ReadOnlyArena {
        public override val height: Int = arena.height
        public override val width: Int = arena.width
        public override val goalWidth: Int = arena.goalWidth

        public override fun getCoordinates(obj: PlayerLogic): #(Int, Int) =
            invertCoordinates(arena, arena.getCoordinates(obj))

        public override fun getCellStatus(position: #(Int, Int)): CellStatus =
            arena.getCellStatus(invertCoordinates(arena, position), player)

        public override fun getBallCoordinates(): #(Int, Int) =
            invertCoordinates(arena, arena.getBallCoordinates())
    }

    public override fun action(arena: Arena): Action {
        var position = arena.getCoordinates(this)
        var readOnlyArena: ReadOnlyArena = arena
        if (invertCoordinates) {
            position = invertCoordinates(arena, position)
            readOnlyArena = InvertCoordinatesArena(this, arena)
        }

        val action = logic.action(position, readOnlyArena)

        return if (invertCoordinates) action.invert(arena) else action
    }

    public override fun getInitPosition(arena: Arena): #(Int, Int) =
    if (invertCoordinates)
        invertCoordinates(arena, logic.getInitPosition(InvertCoordinatesArena(this, arena)))
    else
        logic.getInitPosition(arena)

    public fun equals(obj: Any?): Boolean =
    this === obj || logic === obj
}