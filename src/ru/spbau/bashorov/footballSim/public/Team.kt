package ru.spbau.bashorov.footballSim.public

public trait Team {
    public val name: String
    public fun getPlayers(): Array<PlayerBehavior>
}