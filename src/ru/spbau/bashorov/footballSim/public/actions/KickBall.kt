package ru.spbau.bashorov.footballSim.public

public class KickBall(public val direction: Direction): Action {
    internal override fun invert(arena: ReadOnlyArena): Action = KickBall(direction.invert())
}

public enum open class Direction {
    NOWHERE
    WEST
    EAST
    NORTH:     Direction() { public override fun invert() = SOUTH }
    SOUTH:     Direction() { public override fun invert() = NORTH }
    NORTHWEST: Direction() { public override fun invert() = SOUTHWEST }
    NORTHEAST: Direction() { public override fun invert() = SOUTHEAST }
    SOUTHWEST: Direction() { public override fun invert() = NORTHWEST }
    SOUTHEAST: Direction() { public override fun invert() = NORTHEAST }

    public open fun invert(): Direction = this
}