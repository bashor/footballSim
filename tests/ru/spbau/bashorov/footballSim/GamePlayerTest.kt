package ru.spbau.bashorov.footballSim

import org.junit.Before as before
import org.junit.Test as test
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.actions.Action

public class GamePlayerTest: GamePlayerTestBase() {
    override val player: GamePlayer = GamePlayer(team, playerBehavior, 'A')

    override val returnAction: Action = mock(javaClass<Action>())
    override val expectedAction: Action = returnAction
    override val actionPosition: Pair<Int, Int> = Pair(0, 0)

    override val returnPosition: Pair<Int, Int> = Pair(1, 3)
    override val expectedPosition: Pair<Int, Int> = returnPosition
}