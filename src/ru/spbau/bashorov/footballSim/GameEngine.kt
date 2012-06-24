package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.utils.shuffle

class GameEngine (val firstTeam: Team, val secondTeam: Team, val arena: Arena, val matchDuration: Int) {
    private class object {
        private val FIRST_TEAM_SYMBOLS  = "1234567890#$"
        private val SECOND_TEAM_SYMBOLS = "ABCDEFGHKLMN"
    }

    private val activeObjects = ArrayList<GameObject>()
    private val ball = Ball()
    private var firstTeamScore = 0
    private var secondTeamScore = 0
    private var whoLastKickBall: Player? = null

    static {
        val firstTeamPlayers = firstTeam.getPlayers().copyOf()
        val secondTeamPlayers = secondTeam.getPlayers().copyOf()

        if (firstTeamPlayers.size != secondTeamPlayers.size) {
            throw Exception("size")
        }

        registerPlayers(firstTeam, firstTeamPlayers, FIRST_TEAM_SYMBOLS, false)
        registerPlayers(secondTeam, secondTeamPlayers, SECOND_TEAM_SYMBOLS, true)

        activeObjects.add(ball)

        arena.addObjects(activeObjects)
        arena.resetObjectsPostions()
        arena.moveBallNearestTo({o -> o is Player && (o as Player).team === firstTeam})

        arena.addGoalListener({
            val y = arena.getBallCoordinates()._2
            var team = firstTeam
            if (y <= 0) {
                secondTeamScore++
            }
            else if (y >= arena.height - 1) {
                firstTeamScore++
                team = secondTeam
            }

            arena.resetObjectsPostions()
            arena.moveBallNearestTo({o -> o is Player && (o as Player).team === team})
        })

        arena.addOutListener({
            var team = if (whoLastKickBall?.team == secondTeam) firstTeam else secondTeam
            arena.moveBallNearestTo({o -> o is Player && (o as Player).team === team})
        })
    }

    public fun run() {
        arena.print(firstTeam.name, firstTeamScore, secondTeam.name, secondTeamScore)
        for (time in 0..matchDuration) {
            runOnce()
        }
    }

    private fun runOnce() {
        activeObjects.shuffle()
        for (activeObject in activeObjects) {
            runAction(activeObject)
            arena.print(firstTeam.name, firstTeamScore, secondTeam.name, secondTeamScore)
        }
//        arena.print(firstTeam.name, firstTeamScore, secondTeam.name, secondTeamScore)
    }

    fun runAction(activeObject: GameObject) {
        val action = activeObject.action(arena)
        when (action) {
            is Move -> {
                arena.move(activeObject, action.position)
            }
            is KickBall -> {
                whoLastKickBall = activeObject as Player
                ball.kick(action)
                runAction(ball)
            }
            is Nothing -> {/*Do Nothing*/}
            else -> throw Exception("beda")
        }
    }

    fun registerPlayers(team: Team, players: Array<PlayerLogic>, teamSymbols: String, invertCoordinates: Boolean) {
        var i = 0
        players forEach {
            activeObjects.add(Player(team, it, teamSymbols[i++], invertCoordinates))
        }
    }
}
