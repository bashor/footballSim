package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*

public class GameEngine (val firstTeam: Team, val secondTeam: Team, val arena: Arena) {
    private val FIRST_TEAM_SYMBOLS  = "1234567890#$"
    private val SECOND_TEAM_SYMBOLS = "ABCDEFGHKLMN"

    private val activeObjects = ArrayList<GameObject>()
    private val ball = Ball()

    static {
        val firstTeamPlayers = firstTeam.getPlayers()
        val secondTeamPlayers = secondTeam.getPlayers()

        if (firstTeamPlayers.size != secondTeamPlayers.size) {
            throw Exception("size")
        }

        fun registerPlayers(players: Array<PlayerLogic>, teamSymbols: String) {
            var i = 0
            players forEach {
                activeObjects.add(Player(it, teamSymbols[i++]))
            }
        }

        registerPlayers(firstTeamPlayers, FIRST_TEAM_SYMBOLS)
        registerPlayers(secondTeamPlayers, SECOND_TEAM_SYMBOLS)

        activeObjects.add(ball)

        arena.addObjects(activeObjects)
    }
    public fun run() {
        while (true) {
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

private class Player(val logic: PlayerLogic, override val sym: Char) : GameObject {
    public override fun action(arena: Arena): Action =
        logic.action(arena.getCoordinates(this), arena)

    public fun equals(obj: Any?): Boolean =
        this === obj || logic === obj
}