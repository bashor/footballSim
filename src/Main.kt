package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.CellStatus

fun main(args: Array<String>) {
    GameEngine(SimpleTeam("One"), SimpleTeam("Two"), Arena(height = 11, width = 11, goalWidth = 5), 200).run()
}