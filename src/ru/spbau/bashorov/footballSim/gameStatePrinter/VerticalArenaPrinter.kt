package ru.spbau.bashorov.footballSim.gameStatePrinter

import java.io.PrintStream
import ru.spbau.bashorov.footballSim.GameStatePrinter
import ru.spbau.bashorov.footballSim.public.Arena

public class VerticalArenaPrinter(private val out: PrintStream = System.out): GameStatePrinter {
    class object {
        private val GOAL = '-'
    }
    public override fun print(arena: Arena, team1Name: String, team1Score: Int, team2Name: String, team2Score: Int) {

        val goalStart = (arena.width - arena.goalWidth) / 2
        val goalEnd = goalStart + arena.goalWidth

        fun line (cornerLeft: Char, cornerRight: Char) {
            out.print(cornerLeft)
            for (j in 0..goalStart - 1) {
                out.print(BORDER_HORIZONTAL)
            }
            for (j in goalStart..goalEnd - 1) {
                out.print(GOAL)
            }
            for (j in goalEnd..arena.width - 1) {
                out.print(BORDER_HORIZONTAL)
            }
            out.println(cornerRight)
        }

        out.println("$team1Name $team1Score - $team2Score $team2Name")

        line(CORNER_TOP_LEFT, CORNER_TOP_RIGHT)

        for (i in 0..arena.height - 1) {
            out.print(BORDER_VERTICAL)
            for (j in 0..arena.width - 1) {
                out.print(arena[j, i].sym)
            }
            out.println(BORDER_VERTICAL)
        }

        line(CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT)
    }
}