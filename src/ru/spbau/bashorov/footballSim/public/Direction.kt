package ru.spbau.bashorov.footballSim.public

import java.util.Map

public enum class Direction(public val shift: #(Int, Int)) {
    NOWHERE:       Direction(#( 0, 0))
    LEFT:          Direction(#(-1, 0))
    RIGHT:         Direction(#(+1, 0))
    FORWARD:       Direction(#( 0, 1))
    BACKWARD:      Direction(#( 0,-1))
    FORWARD_LEFT:   Direction(#(-1, 1))
    FORWARD_RIGHT:  Direction(#( 1, 1))
    BACKWARD_LEFT:  Direction(#(-1,-1))
    BACKWARD_RIGHT: Direction(#( 1,-1))

    // workaround, because abstract method for enum doesn't work fully now
    public fun invert(): Direction {
        public val INVERT_DIRECTION: Map<Direction, Direction> = hashMap(
                Direction.NOWHERE       to Direction.NOWHERE,
                Direction.LEFT          to Direction.LEFT,
                Direction.RIGHT         to Direction.RIGHT,
                Direction.FORWARD       to Direction.BACKWARD,
                Direction.BACKWARD      to Direction.FORWARD,
                Direction.FORWARD_LEFT   to Direction.BACKWARD_RIGHT,
                Direction.FORWARD_RIGHT  to Direction.BACKWARD_LEFT,
                Direction.BACKWARD_LEFT  to Direction.FORWARD_RIGHT,
                Direction.BACKWARD_RIGHT to Direction.FORWARD_LEFT)

        return INVERT_DIRECTION[this] ?: Direction.NOWHERE
    }
}

public val SHIFT_TO_DIRECTION: Map<#(Int, Int), Direction> = hashMap(
        #( 0, 0) to Direction.NOWHERE,
        #(-1, 0) to Direction.LEFT,
        #(+1, 0) to Direction.RIGHT,
        #( 0, 1) to Direction.FORWARD,
        #( 0,-1) to Direction.BACKWARD,
        #(-1, 1) to Direction.FORWARD_LEFT,
        #( 1, 1) to Direction.FORWARD_RIGHT,
        #(-1,-1) to Direction.BACKWARD_LEFT,
        #( 1,-1) to Direction.BACKWARD_RIGHT)