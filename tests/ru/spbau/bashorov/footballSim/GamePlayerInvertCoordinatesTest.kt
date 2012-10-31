package ru.spbau.bashorov.footballSim

import org.junit.Before as before
import org.junit.Test as test
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.actions.Action

public class GamePlayerInvertCoordinatesTest: GamePlayerTestBase() {
    override val player: GamePlayer = GamePlayerInvertCoordinates(team, playerBehavior, 'A')

    override val returnAction: Action = mock(javaClass<Action>())
    override val expectedAction: Action = mock(javaClass<Action>())
    override val actionPosition: Pair<Int, Int> = Pair(4, 6)   // for arena(height = 7, width = 5)

    override val returnPosition: Pair<Int, Int> = Pair(1, 3)
    override val expectedPosition: Pair<Int, Int> = Pair(3, 3) // for arena(height = 7, width = 5)

    before fun setupAction() {
        ifCall(returnAction.invert(isA(javaClass<Arena>(), arena))).thenReturn(expectedAction)
    }
}