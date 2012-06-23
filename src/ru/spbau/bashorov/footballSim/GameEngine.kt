package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*

class GameEngine (val firstTeam: Team, val secondTeam: Team, val arena: Arena, val matchDuration: Int) {
    private class object {
        private val FIRST_TEAM_SYMBOLS  = "1234567890#$"
        private val SECOND_TEAM_SYMBOLS = "ABCDEFGHKLMN"
    }

    private val activeObjects = ArrayList<GameObject>()
    private val ball = Ball()
    private var firstTeamScore = 0
    private var secondTeamScore = 0

    static {
        val firstTeamPlayers = firstTeam.getPlayers()
        val secondTeamPlayers = secondTeam.getPlayers()

        if (firstTeamPlayers.size != secondTeamPlayers.size) {
            throw Exception("size")
        }

        fun registerPlayers(players: Array<PlayerLogic>, teamSymbols: String, invertCoordinates: Boolean) {
            var i = 0
            players forEach {
                activeObjects.add(Player(it, teamSymbols[i++], invertCoordinates))
            }
        }

        registerPlayers(firstTeamPlayers, FIRST_TEAM_SYMBOLS, false)
        registerPlayers(secondTeamPlayers, SECOND_TEAM_SYMBOLS, true)

        activeObjects.add(ball)

        arena.addObjects(activeObjects)
        arena.resetObjectsPostions()

        arena.addGoalListener({
            val y = arena.getBallCoordinates()._2
            if (y < 0)
                firstTeamScore++
            else if (y >= arena.height)
                secondTeamScore++

            arena.resetObjectsPostions()
        })

        arena.addOutListener({

        })
    }

    public fun run() {
        arena.print(System.out)
        for (time in 0..matchDuration) {
            runOnce()
        }
    }

    private fun runOnce() {
        for (activeObject in activeObjects) {
            runAction(activeObject)
        }
        arena.print(System.out)
    }

    fun runAction(activeObject: GameObject) {
        val action = activeObject.action(arena)
        when (action) {
            is Move -> {
                arena.move(activeObject, action.position)
            }
            is KickBall -> {
                ball.kick(action)
                runAction(ball)
            }
            is Nothing -> {/*Do Nothing*/}
            else -> throw Exception("beda")
        }
    }
}

private class Player(val logic: PlayerLogic, override val sym: Char, private val invertCoordinates: Boolean): GameObject {
    private class InvertCoordinatesArena(private val arena: ReadOnlyArena) : ReadOnlyArena {
        public override val height: Int = arena.height
        public override val width: Int = arena.width
        public override val goalWidth: Int = arena.goalWidth

        public override fun getCoordinates(obj: PlayerLogic): #(Int, Int) =
            invertCoordinates(arena, arena.getCoordinates(obj))

        public override fun cellIsFree(position: #(Int, Int)): Boolean =
            arena.cellIsFree(invertCoordinates(arena, position))

        public override fun getBallCoordinates(): #(Int, Int) =
            invertCoordinates(arena, arena.getBallCoordinates())
    }

    public override fun action(arena: Arena): Action {
        var position = arena.getCoordinates(this)
        var readOnlyArena: ReadOnlyArena = arena
        if (invertCoordinates) {
            position = invertCoordinates(arena, position)
            readOnlyArena = InvertCoordinatesArena(arena)
        }

        val action = logic.action(position, readOnlyArena)

        return if (invertCoordinates) action.invert(arena) else action
    }

    public override fun getInitPosition(arena: Arena): #(Int, Int) =
        if (invertCoordinates)
            invertCoordinates(arena, logic.getInitPosition(InvertCoordinatesArena(arena)))
        else
            logic.getInitPosition(arena)

    public fun equals(obj: Any?): Boolean =
        this === obj || logic === obj
}