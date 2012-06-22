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
        override val initPosition: #(Int, Int) = #(1, 2)
    }

    val team = array<PlayerLogic>(Logic(this), Logic(this), Logic(this), Logic(this))

    override fun getPlayers(): Array<PlayerLogic> = team
}