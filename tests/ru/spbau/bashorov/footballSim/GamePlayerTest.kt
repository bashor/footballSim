package ru.spbau.bashorov.footballSim

import org.junit.Before as before
import org.junit.Test as test
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.actions.Action

public class GamePlayerTest: GamePlayerTestBase() {
    override val player: GamePlayer = GamePlayer(team, playerBehavior, 'A')

    override val returnAction: Action = mock(javaClass<Action>())
    override val expectedAction: Action = returnAction
    override val actionPosition: #(Int, Int) = #(0, 0)

    override val returnPosition: #(Int, Int) = #(1, 3)
    override val expectedPosition: #(Int, Int) = returnPosition
}