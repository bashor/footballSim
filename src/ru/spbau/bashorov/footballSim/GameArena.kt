package ru.spbau.bashorov.footballSim

import ru.spbau.bashorov.footballSim.public.GameObject
import ru.spbau.bashorov.footballSim.public.ActiveObject
import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.Arena
import java.util.List

trait GameArena: Arena {
    public fun addActiveObjects(objects: List<ActiveObject>)
    public fun addGoalListener(handler: ()->Unit)
    public fun addOutListener(handler: ()->Unit)
    public fun resetObjectsPositions()
    public fun move(activeObject: GameObject, position: #(Int, Int))
    public fun moveBallNearestTo(checker: (GameObject)->Boolean)
}