package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.Arena

public trait GameStatePrinter {
    public fun print(arena: Arena, team1Name: String, team1Score: Int, team2Name: String, team2Score: Int)
}