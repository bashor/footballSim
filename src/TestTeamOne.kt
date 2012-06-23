import ru.spbau.bashorov.footballSim.public.*
import ru.spbau.bashorov.footballSim.public.utils.*

public class TestTeamOne: Team {
    // parent is workaround, because *inner* annotation still does not work
    /*inner*/ class Logic(val parent: Team): PlayerLogic {
        override fun action(position: #(Int, Int), arena: ReadOnlyArena): Action {
            if (arena.getBallCoordinates().isApproachableFrom(position))
                return KickBall(Direction.NORTHEAST)
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

    val team = array<PlayerLogic>(Logic(this), Logic(this), Logic(this), Logic(this))

    override fun getPlayers(): Array<PlayerLogic> = team
}