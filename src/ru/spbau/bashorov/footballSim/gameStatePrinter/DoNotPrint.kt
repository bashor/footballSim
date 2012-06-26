package ru.spbau.bashorov.footballSim.gameStatePrinter

import ru.spbau.bashorov.footballSim.GameStatePrinter
import ru.spbau.bashorov.footballSim.public.Arena

public class DoNotPrint: GameStatePrinter {
    public override fun print(arena: Arena, team1Name: String, team1Score: Int, team2Name: String, team2Score: Int) {
        // Do Nothing
    }
}