import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.actions.*
import ru.spbau.bashorov.footballSim.public.exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.exceptions.PlayerBehaviorException
import ru.spbau.bashorov.footballSim.public.gameObjects.Free
import ru.spbau.bashorov.footballSim.public.utils.*

public class SimpleTeam(public override val name: String): Team {
    class SimplePlayer(): PlayerBehavior {
        override fun action(position: Pair<Int, Int>, arena: Arena): Action {
            val ballPosition = arena.getBallCoordinates()
            if (ballPosition.isAchievableFrom(position)) {
                val goalStart = (arena.width - arena.goalWidth) / 2
                val goalEnd = goalStart + arena.goalWidth

                var ballNewPosition: Pair<Int, Int>? = null
                if (ballPosition.second == arena.height - 1 && ballPosition.first >= goalStart && ballPosition.first < goalEnd) {
                    ballNewPosition = ballPosition + Pair(0, 1)
                } else {
                    try {
                        ballNewPosition = stepTo(ballPosition, Pair(arena.width / 2, arena.height), arena)
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
                return DoNothing()
            }
        }

        public override fun getInitPosition(arena: Arena): Pair<Int, Int> {
            val center = arena.width / 2
            for (i in 0..arena.height) {
                for (j in -i..i) {
                    val pos = Pair(center + j, i)
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