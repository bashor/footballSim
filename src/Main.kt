package ru.spbau.bashorov.footballSim

fun main(args: Array<String>) {
    GameEngine(SimpleTeam("Numbers"), SimpleTeam("Letters"), GameArena(height = 11, width = 15, goalWidth = 9), 200, 100).run()
}