package ru.spbau.bashorov.footballSim

public trait PlayerLogic {
    fun action() : Action
    val initPosition: #(Int, Int)
}