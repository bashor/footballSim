package ru.spbau.bashorov.footballSim

fun main(args: Array<String>) {
    GameEngine(SimpleTeam("One"), SimpleTeam("Two"), GameArena(height = 11, width = 11, goalWidth = 5), 200).run()
}