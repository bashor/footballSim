import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*

public class SimpleTeam(public override val name: String): Team {
    // parent is workaround, because *inner* annotation still does not work
    /*inner*/ class Logic(val parent: Team): PlayerLogic {
        override fun action(position: #(Int, Int), arena: ReadOnlyArena): Action {
            fun moveToIfFree(to: #(Int,Int)): Boolean {
                try {
                    return arena.cellIsFree(to)
                } catch (e: Exception) {}
                return false
            }

            val ballPosition = arena.getBallCoordinates()
            if (ballPosition.isApproachableFrom(position)){
                try {
                    val ballNewPosition = stepTo(ballPosition, #(arena.width / 2, arena.height - 1), arena)
                    val diff = #(ballNewPosition._1 - ballPosition._1, ballNewPosition._2 - ballPosition._2)
                    val direction = SHIFT_TO_DIRECTION[diff]
                    if (direction != null) {
                        return KickBall(direction)
                    }
                } catch (e: Exception) {}
            }

            try {
                return Move(stepTo(position, ballPosition, arena))
            } catch (e: Exception) {}

            return Nothing()
        }

        public override fun getInitPosition(arena: ReadOnlyArena): #(Int, Int) {
            val center = arena.width / 2
            for (i in 0..arena.height) {
                for (j in -i..i) {
                    val pos = #(center + j, i)
                    if (arena.cellIsFree(pos)) {
                        return pos
                    }
                }
            }
            throw Exception("pe4al'")
        }
    }

    val team = array<PlayerLogic>(Logic(this)
            , Logic(this), Logic(this), Logic(this)
    )

    override fun getPlayers(): Array<PlayerLogic> = team
}