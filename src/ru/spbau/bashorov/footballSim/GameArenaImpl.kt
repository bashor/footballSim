package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.exceptions.*
import ru.spbau.bashorov.footballSim.public.gameObjects.Free
import ru.spbau.bashorov.footballSim.public.utils.*

class GameArenaImpl (
        public override val height: Int,
        public override val width: Int,
        public override val goalWidth: Int): GameArena {
    {
        if (height % 2 == 0 || height <= 0) {
            throw IllegalArgumentException("height($height) must be odd and greater than 0.")
        }
        if (width % 2 == 0 || width <= 0) {
            throw IllegalArgumentException("width($width) must be odd and greater than 0.")
        }
        if (goalWidth % 2 == 0 || goalWidth > width) {
            throw IllegalArgumentException("goalWidth($goalWidth) must be odd and greater than width.")
        }
    }

    private val cells = Array<Array<GameObject>>(height, {Array<GameObject>(width, { Free() }) })

    private val activeObjects = ArrayList<ActiveObject>()

    private var ball: Ball? = null

    public override fun get(x: Int, y: Int): GameObject = cells[y][x]
    private fun set(p: Pair<Int, Int>, v: GameObject) = set(p.first, p.second, v)
    private fun set(x: Int, y: Int, v: GameObject) = cells[y][x] = v

    public override fun addActiveObjects(objects: List<ActiveObject>) {
        activeObjects.addAll(objects)
        val found = activeObjects.find({ it is Ball })
        if (found == null)
            throw BallNotFoundException()
        ball = found as Ball
    }

    private val goalListeners = ArrayList<()->Unit>()
    public override fun addGoalListener(handler: ()->Unit) {
        goalListeners.add(handler)
    }

    private val outListeners = ArrayList<()->Unit>()
    public override fun addOutListener(handler: ()->Unit) {
        outListeners.add(handler)
    }

    private fun notify(listeners: List<()->Unit>) = listeners.forEach({ it() })

    public override fun resetObjectsPositions() {
        for (i in cells.indices) {
            for (j in cells[i].indices) {
                cells[i][j] = Free()
            }
        }

        for (obj in activeObjects) {
            val position = obj.getInitPosition(this)
            if (this[position] !is Free)
                throw PlayerBehaviorException("Position $position is not free.")

            this[position] = obj
        }
    }

    private val goalStart = (width - goalWidth) / 2
    private val goalEnd = goalStart + goalWidth

    public override fun move(activeObject: GameObject, position: Pair<Int, Int>) {
        var goal = false
        var out = false
        if (position.first < 0 || position.first >= width) {
            if (activeObject !== ball)
                throw IllegalArgumentException("Bad position ${position} for moving.")
            out = true
        }
        if ( position.second < 0 || position.second >= height)
        {
            if (activeObject !== ball)
                throw IllegalArgumentException("Bad position ${position} for moving.")

            goal = position.first >= goalStart && position.first < goalEnd
            out = !goal
        }
        if (!goal && !out && this[position] !is Free) {
            throw CanNotMoveToPositionException("Can not move to ${position}, position doesn't free.")
        }

        val currentPosition = getCoordinates(activeObject)

        if (!(currentPosition isAchievableFrom position)) {
            throw CanNotMoveToPositionException("Can not move to ${position}, position doesn't achievable from current position $currentPosition.")
        }

        if (goal)
            notify(goalListeners)
        else if (out)
            notify(outListeners)
        else
            moveObject(currentPosition, position)
    }

    private fun moveObject(currentPosition: Pair<Int, Int>, newPosition: Pair<Int, Int>) {
        if (currentPosition == newPosition)
            return
        this[newPosition] = this[currentPosition]
        this[currentPosition] = Free()
    }

    public override fun moveBallNearestTo(checker: (GameObject)->Boolean) {
        fun tryMoveBallNear(o: GameObject): Boolean {
            val ballPosition = getCoordinates(ball!!)
            val position = getCoordinates(o)

            try {
                val to = stepTo(position, Pair(position.first, height / 2), this)
                moveObject(ballPosition, to)
                return true
            } catch (e: AchievablePositionNotFoundException){
                return false
            }
        }

        getObjectNearestTo(ball!!, {o -> checker(o) && tryMoveBallNear(o) })
    }

    private fun getObjectNearestTo(obj: GameObject, checker: (GameObject)->Boolean): GameObject {
        val objPosition = getCoordinates(obj)
        activeObjects.sort(comparator<GameObject> { (a, b) -> (getCoordinates(a).calcDistanceTo(objPosition)).compareTo(getCoordinates(b).calcDistanceTo(objPosition)) })
        for (ao in activeObjects) {
            if (checker(ao)) {
                return ao
            }
        }
        throw AchievablePositionNotFoundException()
    }
}