package ru.spbau.bashorov.footballSim

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Before as before
import org.junit.Test as test
import org.mockito.Mockito.*
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.PlayerBehavior
import ru.spbau.bashorov.footballSim.public.Team
import ru.spbau.bashorov.footballSim.public.actions.Action

private abstract class GamePlayerTestBase {
    abstract val returnAction: Action
    abstract val expectedAction: Action
    abstract val actionPosition: #(Int, Int)
    abstract val returnPosition: #(Int, Int)
    abstract val expectedPosition: #(Int, Int)
    abstract val player: GamePlayer

    before val playerBehavior = mock(javaClass<PlayerBehavior>())
    before val team = mock(javaClass<Team>())
    before val arena = mock(javaClass<Arena>())

    test fun actionCall() {
        ifCall(playerBehavior.action(eq(actionPosition), isA(javaClass<Arena>(), arena))).thenReturn(returnAction)

        val returnedAction = player.action(arena)

        assertTrue(expectedAction === returnedAction)
        verify(playerBehavior, times(1)).action(eq(actionPosition), isA(javaClass<Arena>(), arena))
        verify(playerBehavior, never()).action(isA(javaClass<#(Int,Int)>(), actionPosition), eq(arena))
        verify(playerBehavior, never()).getInitPosition(arena)
    }

    test fun getInitPositionCall() {
        ifCall(playerBehavior.getInitPosition(isA(javaClass<Arena>(), arena))).thenReturn(returnPosition)

        val returnedPosition = player.getInitPosition(arena)

        assertEquals(expectedPosition, returnedPosition)
        verify(playerBehavior, times(1)).getInitPosition(isA(javaClass<Arena>(), arena))
        verify(playerBehavior, never()).getInitPosition(arena)
        verify(playerBehavior, never()).action(isA(javaClass<#(Int,Int)>(), #(0, 0)), isA(javaClass<Arena>(), arena))
    }

    before fun initEnvironment() {
        ifCall(arena.width).thenReturn(5)
        ifCall(arena.height).thenReturn(7)
        ifCall(arena.get(0,0)).thenReturn(player)
    }
}