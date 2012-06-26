package ru.spbau.bashorov.footballSim.gameStatePrinter

import ru.spbau.bashorov.footballSim.GameStatePrinter
import ru.spbau.bashorov.footballSim.public.Arena
import java.io.PrintStream

public class HorizontalArenaPrinter(private val out: PrintStream = System.out): GameStatePrinter {
    class object {
        private val GOAL = ':'
    }
    public override fun print(arena: Arena, team1Name: String, team1Score: Int, team2Name: String, team2Score: Int) {

        private val goalStart = (arena.width - arena.goalWidth) / 2
        private val goalEnd = goalStart + arena.goalWidth

        fun line (cornerLeft: Char, cornerRight: Char) {
            out.print(cornerLeft)
            for (j in 0..arena.height- 1) {
                out.print(BORDER_HORIZONTAL)
            }
            out.println(cornerRight)
        }

        out.println("$team1Name $team1Score - $team2Score $team2Name")

        line(CORNER_TOP_LEFT, CORNER_TOP_RIGHT)

        for (i in 0..arena.width - 1) {
            fun printVerticalBorder() {
                if (i >= goalStart && i < goalEnd) {
                    out.print(GOAL)
                } else {
                    out.print(BORDER_VERTICAL)
                }
            }
            printVerticalBorder()
            for (j in 0..arena.height- 1) {
                out.print(arena[i, j].sym)
            }
            printVerticalBorder()
            out.println()
        }

        line(CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT)
    }
}