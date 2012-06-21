import ru.spbau.bashorov.footballSim.Team
import ru.spbau.bashorov.footballSim.PlayerLogic
import ru.spbau.bashorov.footballSim.Action
import ru.spbau.bashorov.footballSim.Nothing

public class TestTeamOne : Team {

    class Logic : PlayerLogic {
        override fun action() : Action {
            return Nothing()//Move(#(1,0))
        }
        override val initPosition: #(Int, Int) = #(1, 2)

    }

    override fun getPlayers(): Array<PlayerLogic> = array(Logic(), Logic(), Logic(), Logic())
}