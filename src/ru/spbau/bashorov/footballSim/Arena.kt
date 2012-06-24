package ru.spbau.bashorov.footballSim

import java.io.PrintStream
import java.util.ArrayList
import java.util.List
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*
import ru.spbau.bashorov.footballSim.utils.*

class Arena (
        public override val height: Int,
        public override val width: Int,
        public override val goalWidth: Int): ReadOnlyArena {
    {
        if (height % 2 == 0 || height <= 0) {
            throw IllegalArgumentException("height($height) must be odd and greater than 0")
        }
        if (width % 2 == 0 || width <= 0) {
            throw IllegalArgumentException("width($width) must be odd and greater than 0")
        }
        if (goalWidth % 2 == 0 || goalWidth > width) {
            throw IllegalArgumentException("goalWidth($goalWidth) must be odd and greater than width")
        }
    }

    private val cells = Array<Array<GameObject?>>(height, {arrayOfNulls<GameObject>(width)})

    private val activeObjects = ArrayList<GameObject>()

    private var ball: Ball? = null

    public fun addObjects(objects: ArrayList<GameObject>) {
        activeObjects.addAll(objects)
        ball = activeObjects.find({it is Ball}) as Ball
    }

    private val goalListeners: List<()->Unit> = ArrayList<()->Unit>()
    public fun addGoalListener(handler: ()->Unit) {
        goalListeners.add(handler)
    }

    fun fireGoal() {
        for (h in goalListeners) {
            h()
        }
    }

    private val outListeners: List<()->Unit> = ArrayList<()->Unit>()
    fun addOutListener(handler: ()->Unit) {
        outListeners.add(handler)
    }

    fun fireOut() {
        for (h in outListeners) {
            h()
        }
    }

    public fun resetObjectsPostions() {
        for (i in cells.indices) {
            for (j in cells[i].indices) {
                cells[i][j] = null
            }
        }

        for (obj in activeObjects) {
            val position = obj.getInitPosition(this)
            if (getCellStatus(position) != CellStatus.FREE)
                throw Exception("занято")

            cells[position._2][position._1] = obj

            //TODO: проверять что игрок стоит на совей половине поля
        }
    }

    private val goalStart = (width - goalWidth) / 2
    private val goalEnd = goalStart + goalWidth

    public fun move(activeObject: GameObject, position: #(Int, Int)) {
        var goal = false
        var out = false
        if (position._1 < 0 || position._1 >= width) {
            if (activeObject !== ball)
                throw IllegalArgumentException("Can not move to ${position}")
            out = true
        }
        if ( position._2 < 0 || position._2 >= height)
        {
            if (activeObject !== ball)
                throw IllegalArgumentException("Can not move to ${position}")

            goal = position._1 >= goalStart && position._1 < goalEnd
            out = !goal
        }
        if (!goal && !out && cells[position._2][position._1] != null) {
            throw Exception("Can not move to ${position}")
        }

        val currentPosition = getCoordinates(activeObject)

        if (!(currentPosition isApproachableFrom position)) {
            throw Exception("Can not move to ${position}")
        }

        if (goal)
            fireGoal()
        else if (out)
            fireOut()
        else
            moveObject(currentPosition, position)
    }

    private fun <T>getCoordinatesHelper(obj: T): #(Int, Int) {
        for (j in cells.indices) {
            for (i in cells[j].indices) {
                if (cells[j][i] == obj)
                    return #(i, j)
            }
        }
        throw Exception("object not found")
    }

    override public fun getCoordinates(obj: PlayerLogic): #(Int, Int) {
        return getCoordinatesHelper(obj)
    }

    public fun getCoordinates(obj: GameObject): #(Int, Int) {
        return getCoordinatesHelper(obj)
    }

    public override fun getBallCoordinates(): #(Int, Int) {
        if (ball === null) {
            throw Exception("Ball not found")
        }
        return getCoordinates(ball!!);
    }

    public override fun getCellStatus(position: #(Int, Int)): CellStatus =
        getCellStatus(position, null)

    public fun getCellStatus(position: #(Int, Int), player: Player?): CellStatus {
        if (position._1 < 0 || position._1 >= width || position._2 < 0 || position._2 >= height) {
            return CellStatus.UNACHIEVABLE
        }

        return when (cells[position._2][position._1]) {
            null -> CellStatus.FREE
            is Ball -> CellStatus.BALL
            is Player -> {
                if (player == null) {
                    CellStatus.OCCUPIED
                }
                else {
                    val otherPlayer = cells[position._2][position._1] as Player
                    if (player.team === otherPlayer.team) CellStatus.PARTNER else CellStatus.OPPONENT
                }
            }
            else -> CellStatus.UNACHIEVABLE
        }
    }

    private fun moveObject(currentPosition: #(Int, Int), newPosition: #(Int, Int)) {
        cells[newPosition._2][newPosition._1] = cells[currentPosition._2][currentPosition._1]
        cells[currentPosition._2][currentPosition._1] = null
    }

    public fun moveBallNearestTo(checker: (GameObject)->Boolean) {
        fun tryMoveBallNear(o: GameObject): Boolean {
            val ballPosition = getBallCoordinates()
            val position = getCoordinates(o)

            try {
                val to = stepTo(position, #(position._1, height / 2), this)
                moveObject(ballPosition, to)
                return true
            } catch (e: Exception){}

            return false
        }

        getObjectNearestTo(ball!!, {o -> checker(o) && tryMoveBallNear(o) })
    }

    public fun getObjectNearestTo(obj: GameObject, checker: (GameObject)->Boolean): GameObject {
        val objPosition = getCoordinates(obj)
        activeObjects.sort({a, b -> (getCoordinates(a) - objPosition).compareTo(getCoordinates(b) - objPosition)})
        for (ao in activeObjects) {
            if (checker(ao)) {
                return ao
            }
        }
        throw Exception()
    }

    public fun print(team1Name: String, team1Score: Int, team2Name: String, team2Score: Int, out: PrintStream = System.out) {

        private val BORDER_HORIZONTAL   = '\u2501'
        private val BORDER_VERTICAL     = '\u2503'

        private val CORNER_TOP_LEFT     = '\u250F'
        private val CORNER_TOP_RIGHT    = '\u2513'
        private val CORNER_BOTTOM_LEFT  = '\u2517'
        private val CORNER_BOTTOM_RIGHT = '\u251B'

        private val PLACEHOLDER         = '\uE146'
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

        out.println(team1Score.toString() + "\t" + team1Name)
        out.println(team2Score.toString() + "\t" + team2Name)

        line(CORNER_TOP_LEFT, CORNER_TOP_RIGHT)

        for (line in cells) {
            out.print(BORDER_VERTICAL)
            for (cell in line) {
                if (cell == null)
                    out.print(PLACEHOLDER)
                else
                    out.print(cell.sym)
            }
            out.println(BORDER_VERTICAL)
        }

        line(CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT)
    }
}