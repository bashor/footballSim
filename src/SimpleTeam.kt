import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.exceptions.PlayerBehaviorException
import ru.spbau.bashorov.footballSim.public.utils.*
import ru.spbau.bashorov.footballSim.Free

public class SimpleTeam(public override val name: String): Team {
    class SimplePlayer(): PlayerBehavior {
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
                    if (arena[pos] is Free) {
                        return pos
                    }
                }
            }
            throw PlayerBehaviorException("ну не шмогла я...")
        }
    }

    val team = Array<PlayerBehavior>(6, { SimplePlayer() })
//             array<PlayerBehavior>(Logic(), Logic(), Logic(), Logic())

    override fun getPlayers(): Array<PlayerBehavior> = team
}