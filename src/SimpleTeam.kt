import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.Exceptions.AchievablePositionNotFoundException
import ru.spbau.bashorov.footballSim.public.utils.*

public class SimpleTeam(public override val name: String): Team {
    class Logic(): Player {
        override fun action(position: #(Int, Int), arena: Arena): Action {
            val ballPosition = arena.getBallCoordinates()
            if (ballPosition.isAchievableFrom(position)){
                try {
                    val ballNewPosition = stepTo(ballPosition, #(arena.width / 2, arena.height - 1), arena)
                    val diff = #(ballNewPosition._1 - ballPosition._1, ballNewPosition._2 - ballPosition._2)
                    val direction = SHIFT_TO_DIRECTION[diff]
                    if (direction != null) {
                        return KickBall(direction)
                    }
                } catch (e: AchievablePositionNotFoundException) {}
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

    val team = array<Player>(Logic(), Logic(), Logic(), Logic())

    override fun getPlayers(): Array<Player> = team
}