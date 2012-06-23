package ru.spbau.bashorov.footballSim

import java.io.PrintStream
import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*
import java.util.List

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
        for (h in goalListeners) {
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
            if (!cellIsFree(position))
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
        if (cells[position._2][position._1] != null) {
            throw Exception("Can not move to ${position}")
        }

        val currentPosition = getCoordinates(activeObject)

        if (!(currentPosition isApproachableFrom position)) {
            throw Exception("can not move to ${position}")
        }

        cells[position._2][position._1] = cells[currentPosition._2][currentPosition._1]
        cells[currentPosition._2][currentPosition._1] = null

        if (goal)
            fireGoal()
        else if (out)
            fireOut()
    }

    private fun <T>getCoordinatesHelper(obj: T): #(Int, Int) {
        for (i in cells.indices) {
            for (j in cells[i].indices) {
                if (cells[i][j] == obj)
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

    override public fun cellIsFree(position: #(Int, Int)): Boolean {
        if (position._1 < 0 || position._1 >= width || position._2 < 0 || position._2 >= height) {
            throw IllegalArgumentException("")
        }

        return cells[position._2][position._1] == null
    }

    public fun print(out: PrintStream) {

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