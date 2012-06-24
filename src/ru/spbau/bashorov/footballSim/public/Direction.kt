package ru.spbau.bashorov.footballSim.public

import java.util.Map

public enum class Direction(public val shift: #(Int, Int)) {
    NOWHERE:       Direction(#( 0, 0))
    LEFT:          Direction(#(-1, 0))
    RIGHT:         Direction(#(+1, 0))
    FORWARD:       Direction(#( 0, 1))
    BACKWARD:      Direction(#( 0,-1))
    FORWARDLEFT:   Direction(#(-1, 1))
    FORWARDRIGHT:  Direction(#( 1, 1))
    BACKWARDLEFT:  Direction(#(-1,-1))
    BACKWARDRIGHT: Direction(#( 1,-1))

    // workaround, because abstract method for enum dosn't work fully now
    public fun invert(): Direction {
        public val INVERT_DIRECTION: Map<Direction, Direction> = hashMap(
                Direction.NOWHERE       to Direction.NOWHERE,
                Direction.LEFT          to Direction.LEFT,
                Direction.RIGHT         to Direction.RIGHT,
                Direction.FORWARD       to Direction.BACKWARD,
                Direction.BACKWARD      to Direction.FORWARD,
                Direction.FORWARDLEFT   to Direction.BACKWARDRIGHT,
                Direction.FORWARDRIGHT  to Direction.BACKWARDLEFT,
                Direction.BACKWARDLEFT  to Direction.FORWARDRIGHT,
                Direction.BACKWARDRIGHT to Direction.FORWARDLEFT)

        return INVERT_DIRECTION[this] ?: Direction.NOWHERE
    }
}

public val SHIFT_TO_DIRECTION: Map<#(Int, Int), Direction> = hashMap(
        #( 0, 0) to Direction.NOWHERE,
        #(-1, 0) to Direction.LEFT,
        #(+1, 0) to Direction.RIGHT,
        #( 0, 1) to Direction.FORWARD,
        #( 0,-1) to Direction.BACKWARD,
        #(-1, 1) to Direction.FORWARDLEFT,
        #( 1, 1) to Direction.FORWARDRIGHT,
        #(-1,-1) to Direction.BACKWARDLEFT,
        #( 1,-1) to Direction.BACKWARDRIGHT)