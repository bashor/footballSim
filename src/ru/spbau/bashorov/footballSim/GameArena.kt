package ru.spbau.bashorov.footballSim

import java.io.PrintStream
import java.util.ArrayList
import java.util.List
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.exceptions.*
import ru.spbau.bashorov.footballSim.public.utils.*
import ru.spbau.bashorov.footballSim.utils.*

class GameArena (
        public override val height: Int,
        public override val width: Int,
        public override val goalWidth: Int): Arena {
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
    private fun set(p: #(Int, Int), v: GameObject) = set(p._1, p._2, v)
    private fun set(x: Int, y: Int, v: GameObject) = cells[y][x] = v

    public fun addActiveObjects(objects: ArrayList<ActiveObject>) {
        activeObjects.addAll(objects)
        ball = activeObjects.find({ it is Ball }) as Ball
    }

    private val goalListeners = ArrayList<()->Unit>()
    public fun addGoalListener(handler: ()->Unit) {
        goalListeners.add(handler)
    }

    private val outListeners = ArrayList<()->Unit>()
    public fun addOutListener(handler: ()->Unit) {
        outListeners.add(handler)
    }

    private fun notify(listeners: List<()->Unit>) = listeners.forEach({ it() })

    public fun resetObjectsPositions() {
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

    public fun move(activeObject: GameObject, position: #(Int, Int)) {
        var goal = false
        var out = false
        if (position._1 < 0 || position._1 >= width) {
            if (activeObject !== ball)
                throw IllegalArgumentException("Bad position ${position} for moving.")
            out = true
        }
        if ( position._2 < 0 || position._2 >= height)
        {
            if (activeObject !== ball)
                throw IllegalArgumentException("Bad position ${position} for moving.")

            goal = position._1 >= goalStart && position._1 < goalEnd
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

    private fun moveObject(currentPosition: #(Int, Int), newPosition: #(Int, Int)) {
        if (currentPosition == newPosition)
            return
        this[newPosition] = this[currentPosition]
        this[currentPosition] = Free()
    }

    public fun moveBallNearestTo(checker: (GameObject)->Boolean) {
        fun tryMoveBallNear(o: GameObject): Boolean {
            val ballPosition = getCoordinates(ball!!)
            val position = getCoordinates(o)

            try {
                val to = stepTo(position, #(position._1, height / 2), this)
                moveObject(ballPosition, to)
                return true
            } catch (e: AchievablePositionNotFoundException){
                return false
            }
        }

        getObjectNearestTo(ball!!, {o -> checker(o) && tryMoveBallNear(o) })
    }

    public fun getObjectNearestTo(obj: GameObject, checker: (GameObject)->Boolean): GameObject {
        val objPosition = getCoordinates(obj)
        activeObjects.sort({a, b -> (getCoordinates(a).calcDistanceTo(objPosition)).compareTo(getCoordinates(b).calcDistanceTo(objPosition))})
        for (ao in activeObjects) {
            if (checker(ao)) {
                return ao
            }
        }
        throw AchievablePositionNotFoundException()
    }

    public fun print(team1Name: String, team1Score: Int, team2Name: String, team2Score: Int, out: PrintStream = System.out) {

        private val BORDER_HORIZONTAL   = '\u2501' // ━
        private val BORDER_VERTICAL     = '\u2503' // ┃

        private val CORNER_TOP_LEFT     = '\u250F' // ┏
        private val CORNER_TOP_RIGHT    = '\u2513' // ┓
        private val CORNER_BOTTOM_LEFT  = '\u2517' // ┗
        private val CORNER_BOTTOM_RIGHT = '\u251B' // ┛

        private val GOAL                = '-'

        fun line (cornerLeft: Char, cornerRight: Char) {
            out.print(cornerLeft)
            for (j in 0..goalStart - 1) {
                out.print(BORDER_HORIZONTAL)
            }
            for (j in goalStart..goalEnd - 1) {
                out.print(GOAL)
            }
            for (j in goalEnd..width - 1) {
                out.print(BORDER_HORIZONTAL)
            }
            out.println(cornerRight)
        }

        out.println("$team1Name $team1Score - $team2Score $team2Name")

        line(CORNER_TOP_LEFT, CORNER_TOP_RIGHT)

        for (line in cells) {
            out.print(BORDER_VERTICAL)
            for (cell in line) {
                out.print(cell.sym)
            }
            out.println(BORDER_VERTICAL)
        }

        line(CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT)
   }
}