import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.Exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.utils.*

public class SimpleTeam(public override val name: String): Team {
    class SimplePlayer(): Player {
        override fun action(position: #(Int, Int), arena: Arena): Action {
            val ballPosition = arena.getBallCoordinates()
            if (ballPosition.isAchievableFrom(position)) {
                val goalStart = (arena.width - arena.goalWidth) / 2
                val goalEnd = goalStart + arena.goalWidth

                var ballNewPosition: #(Int, Int)? = null
                if (ballPosition._2 == arena.height - 1 && ballPosition._1 >= goalStart && ballPosition._1 < goalEnd) {
                    ballNewPosition = ballPosition + #(0,1)
                } else {
                    try {
                        ballNewPosition = stepTo(ballPosition, #(arena.width / 2, arena.height), arena)
                    } catch (e: AchievablePositionNotFoundException) {
                        // loging
                    }
                }
                if (ballNewPosition != null) {
                    val diff = ballNewPosition!! - ballPosition
                    val direction = SHIFT_TO_DIRECTION[diff]
                    if (direction != null) {
                        return KickBall(direction)
                    }
                }
            }

            try {
                return Move(stepTo(position, ballPosition, arena))
            } catch (e: AchievablePositionNotFoundException) {
                return Nothing()
            }
        }

        public override fun getInitPosition(arena: Arena): #(Int, Int) {
            val center = arena.width / 2
            for (i in 0..arena.height) {
                for (j in -i..i) {
                    val pos = #(center + j, i)
                    if (arena.getCellStatus(pos) == CellStatus.FREE) {
                        return pos
                    }
                }
            }
            throw Exception("ну не шмогла я...")
        }
    }

    val team = Array<Player>(6, { SimplePlayer() })
//            array<Player>(Logic(), Logic(), Logic(), Logic())

    override fun getPlayers(): Array<Player> = team
}