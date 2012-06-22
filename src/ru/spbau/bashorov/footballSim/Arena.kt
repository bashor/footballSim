package ru.spbau.bashorov.footballSim

import java.io.PrintStream
import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*

public class Arena (val height: Int, val width: Int): ReadOnlyArena {
    private val BORDER_HORIZONTAL = '\u2501'
    private val BORDER_VERTICAL = '\u2503'

    private val CORNER_TOP_LEFT = '\u250F'
    private val CORNER_TOP_RIGHT = '\u2513'
    private val CORNER_BOTTOM_LEFT = '\u2517'
    private val CORNER_BOTTOM_RIGHT = '\u251B'

    private val PLACEHOLDER = '\uE146'

    private val cells = Array<Array<GameObject?>>(height, {arrayOfNulls<GameObject>(width)})

    private var ball: Ball? = null

    public fun addObjects(objects: ArrayList<GameObject>) {
        for (i in objects.indices) {
            if (objects[i] is Ball) { // KT
                if (ball !== null) {
                    throw Exception("На поле должен быть только один мяч")
                }

                ball = objects[i] as Ball // KT
            }
            cells[i / width] [i % width] = objects[i];
        }
    }

    public fun move(activeObject: GameObject, position: #(Int, Int)) {
        if (position._1 < 0 || position._1 >= width || position._2 < 0 || position._2 >= height) {
            throw IllegalArgumentException("Can not move to ${position}")
        }
        if (cells[position._1][position._2] != null) {
            throw Exception("Can not move to ${position}")
        }

        val currentPosition = getCoordinates(activeObject)

        fun sqr(value: Int): Double = (value * value).toDouble()

        val distance = position - currentPosition

        if (distance > 1) {
            throw Exception("can not move to ${position}")
        }

        cells[position._1][position._2] = cells[currentPosition._1][currentPosition._2]
        cells[currentPosition._1][currentPosition._2] = null
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

        return cells[position._1][position._2] == null
    }

    public fun print(out: PrintStream) {
        fun line (cornerLeft: Char, cornerRight: Char) {
            out.print(cornerLeft)
            for (j in 0..width - 1) {
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