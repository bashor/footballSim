package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.exceptions.PlayerBehaviorException
import ru.spbau.bashorov.footballSim.public.exceptions.UnknownActionException
import ru.spbau.bashorov.footballSim.utils.shuffle
import java.util.List

class GameEngine (val firstTeam: Team, val secondTeam: Team, val arena: GameArena, val matchDuration: Int, private val sleep: Long = 0) {
    private class object {
        private val FIRST_TEAM_SYMBOLS  = "1234567890#$"
        private val SECOND_TEAM_SYMBOLS = "ABCDEFGHKLMN"
    }

    private val activeObjects = ArrayList<ActiveObject>()
    private val ball = Ball()
    private var firstTeamScore = 0
    private var secondTeamScore = 0
    private var whoLastKickBall: GamePlayer? = null

    static {
        val firstTeamPlayers = firstTeam.getPlayers().toList()
        val secondTeamPlayers = secondTeam.getPlayers().toList()

        if (firstTeamPlayers.size != secondTeamPlayers.size) {
            throw IllegalArgumentException("Numbers of the Players in both Teams must be equals")
        }

        registerPlayers(firstTeam, firstTeamPlayers, FIRST_TEAM_SYMBOLS, false)
        registerPlayers(secondTeam, secondTeamPlayers, SECOND_TEAM_SYMBOLS, true)

        activeObjects.add(ball)

        arena.addActiveObjects(activeObjects)

        arena.resetObjectsPositions()
        arena.moveBallNearestTo({o -> o is GamePlayer && (o as GamePlayer).team === firstTeam})

        arena.addGoalListener({
            val y = arena.getCoordinates(ball)._2
            var team = firstTeam
            if (y <= 0) {
                secondTeamScore++
            }
            else if (y >= arena.height - 1) {
                firstTeamScore++
                team = secondTeam
            }

            arena.resetObjectsPositions()
            arena.moveBallNearestTo({o -> o is GamePlayer && (o as GamePlayer).team === team})
        })

        arena.addOutListener({
            var team = if (whoLastKickBall?.team == secondTeam) firstTeam else secondTeam
            arena.moveBallNearestTo({o -> o is GamePlayer && (o as GamePlayer).team === team})
        })
    }

    public fun run() {
        arena.print(firstTeam.name, firstTeamScore, secondTeam.name, secondTeamScore)
        for (time in 0..matchDuration) {
            if (!runOnce())
                return
            if (sleep > 0)
                Thread.sleep(sleep)
        }

        fun printScore() = println("Score:\n${firstTeam.name} $firstTeamScore - $secondTeamScore ${secondTeam.name}")

        fun printResult(winner: Team) {
            println("${winner.name} team won!")
            printScore()
        }
        if (firstTeamScore > secondTeamScore) {
            printResult(firstTeam)
        } else if (firstTeamScore < secondTeamScore) {
            printResult(secondTeam)
        } else {
            println("Draw!")
            printScore()
        }
    }

    private fun runOnce(): Boolean {
        activeObjects.shuffle()
        for (activeObject in activeObjects) {
//            try {
                runAction(activeObject)
//            } catch (e: PlayerBehaviorException) {
//                if (activeObject is GamePlayer) {
//                    fun printResult(winner: Team, loser: Team) {
//                        println("${winner.name} team won! (${loser.name}'s player has performed illegal operation)")
//                    }
//                    if (activeObject.team == firstTeam) {
//                        printResult(secondTeam, firstTeam)
//                        return false
//                    } else if (activeObject.team == secondTeam) {
//                        printResult(firstTeam, secondTeam)
//                        return false
//                    }
//                } else {
//                    throw e
//                }
//            }
            arena.print(firstTeam.name, firstTeamScore, secondTeam.name, secondTeamScore)
        }
        return true
    }

    fun runAction(activeObject: ActiveObject) {
        val action = activeObject.action(arena)
        when (action) {
            is Move -> {
                arena.move(activeObject, action.position)
            }
            is KickBall -> {
                whoLastKickBall = activeObject as GamePlayer
                ball.kick(action)
                runAction(ball)
            }
            is Nothing -> {/*Do Nothing*/}
            else -> throw UnknownActionException()
        }
    }

    fun registerPlayers(team: Team, players: List<PlayerBehavior>, teamSymbols: String, invertCoordinates: Boolean) {
        if (players.size > teamSymbols.size) {
            throw IllegalArgumentException("Too many players in the $team(name=${team.name}) team.")
        }

        var i = 0
        val converter = if (invertCoordinates)
                {(p: PlayerBehavior) -> GamePlayerInvertCoordinates(team, p, teamSymbols[i++])}
            else
                {(p: PlayerBehavior) -> GamePlayerNormal(team, p, teamSymbols[i++])}

        activeObjects.addAll(players.map(converter))
    }
}
