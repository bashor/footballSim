package ru.spbau.bashorov.footballSim

fun main(args: Array<String>) {
    GameEngine(TestTeamOne(), TestTeamOne(), Arena(10, 10)).run()
}