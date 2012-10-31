package ru.spbau.bashorov.footballSim.public

public enum class Direction(public val shift: Pair<Int, Int>) {
    NOWHERE:       Direction(Pair(0, 0))
    LEFT:          Direction(Pair(-1, 0))
    RIGHT:         Direction(Pair(+1, 0))
    FORWARD:       Direction(Pair(0, 1))
    BACKWARD:      Direction(Pair(0, -1))
    FORWARD_LEFT:   Direction(Pair(-1, 1))
    FORWARD_RIGHT:  Direction(Pair(1, 1))
    BACKWARD_LEFT:  Direction(Pair(-1, -1))
    BACKWARD_RIGHT: Direction(Pair(1, -1))

    // workaround, because abstract method for enum doesn't work fully now
    public fun invert(): Direction {
        val INVERT_DIRECTION: Map<Direction, Direction> = hashMap(
                Direction.NOWHERE       to Direction.NOWHERE,
                Direction.LEFT          to Direction.RIGHT,
                Direction.RIGHT         to Direction.LEFT,
                Direction.FORWARD       to Direction.BACKWARD,
                Direction.BACKWARD      to Direction.FORWARD,
                Direction.FORWARD_LEFT   to Direction.BACKWARD_RIGHT,
                Direction.FORWARD_RIGHT  to Direction.BACKWARD_LEFT,
                Direction.BACKWARD_LEFT  to Direction.FORWARD_RIGHT,
                Direction.BACKWARD_RIGHT to Direction.FORWARD_LEFT)

        return INVERT_DIRECTION[this] ?: Direction.NOWHERE
    }
}

public val SHIFT_TO_DIRECTION: Map<Pair<Int, Int>, Direction> = hashMap(
        Pair(0, 0) to Direction.NOWHERE,
        Pair(-1, 0) to Direction.LEFT,
        Pair(+1, 0) to Direction.RIGHT,
        Pair(0, 1) to Direction.FORWARD,
        Pair(0, -1) to Direction.BACKWARD,
        Pair(-1, 1) to Direction.FORWARD_LEFT,
        Pair(1, 1) to Direction.FORWARD_RIGHT,
        Pair(-1, -1) to Direction.BACKWARD_LEFT,
        Pair(1, -1) to Direction.BACKWARD_RIGHT)