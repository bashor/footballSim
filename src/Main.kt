package ru.spbau.bashorov.footballSim

fun main(args: Array<String>) {
    GameEngine(TestTeamOne(), TestTeamOne(), Arena(height = 11, width = 11, goalWidth = 5), 100).run()
}