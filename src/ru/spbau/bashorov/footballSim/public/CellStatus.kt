package ru.spbau.bashorov.footballSim.public

public enum class CellStatus {
    UNACHIEVABLE
    FREE
    BALL
    PARTNER
    OPPONENT
    OCCUPIED

    public fun equals(other: Any?): Boolean {
        return this === other ||
            this === CellStatus.OCCUPIED && other !== CellStatus.FREE ||
            this !== CellStatus.FREE && other === CellStatus.OCCUPIED
    }
}