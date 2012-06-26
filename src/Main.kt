package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.gameStatePrinter.VerticalArenaPrinter

fun main(args: Array<String>) {
    GameEngine(
            firstTeam = SimpleTeam("Numbers"),
            secondTeam = SimpleTeam("Letters"),
            arena = GameArena(height = 11, width = 15, goalWidth = 9),
            matchDuration = 200,
            printer = VerticalArenaPrinter(),
            sleep = 100).run()
}