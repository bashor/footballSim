package ru.spbau.bashorov.footballSim

import java.util.ArrayList
import java.util.List
import kotlin.test.failsWith
import org.junit.Before as before
import org.junit.Test as test
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyListOf
import org.mockito.Matchers.anyString
import org.mockito.Mockito.*
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.ActiveObject
import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.GameObject
import ru.spbau.bashorov.footballSim.public.PlayerBehavior
import ru.spbau.bashorov.footballSim.public.Team
import ru.spbau.bashorov.footballSim.public.actions.DoNothing

public class GameEngineTest {
    test fun ConstructorWithIllegalArgsTest() {
        val team2WithManyPlayers = array<PlayerBehavior>(mock(javaClass<PlayerBehavior>()), mock(javaClass<PlayerBehavior>()))
        ifCall(team2.getPlayers()).thenReturn(team2WithManyPlayers)

        failsWith<IllegalArgumentException> {
            GameEngine(team1, team2, mock(javaClass<GameArena>()), 1)
        }
    }

    test fun ConstructorTest() {
        GameEngine(team1, team2, arena, 1)

        verify(team1, atLeastOnce()).getPlayers()
        verify(team2, atLeastOnce()).getPlayers()
        verify(arena, atLeastOnce()).addActiveObjects(anyListOf(javaClass<ActiveObject>())!!)

        val inOrder = inOrder(arena);
        inOrder.verify(arena, atLeastOnce())?.resetObjectsPositions()
        inOrder.verify(arena, atLeastOnce())?.moveBallNearestTo(isA(javaClass<(GameObject)->Boolean>(), {(GameObject)->true}))

        verify(arena, atLeastOnce()).addGoalListener(isA(javaClass<()->Unit>(), {}))
        verify(arena, atLeastOnce()).addOutListener(isA(javaClass<()->Unit>(), {}))
    }

    test fun durationUsedCorrect() {
        val DURATION = 7;

        GameEngine(team1, team2, arena, DURATION).run()

        verify(team1players[0], times(DURATION)).action(eq(FIRST_PLAYER_POSITION), isA(javaClass<Arena>(), arena))
        verify(team2players[0], times(DURATION)).action(eq(SECOND_PLAYER_RELATIVE_POSITION), isA(javaClass<Arena>(), arena))
    }

    test fun printerCalling() {
        val DURATION = 7;
        val printer = mock(javaClass<GameStatePrinter>())

        GameEngine(team1, team2, arena, DURATION, printer).run()

        verify(printer, atLeast(DURATION)).print(isA(javaClass<Arena>(), arena), anyString()!!, anyInt(), anyString()!!, anyInt())
    }

    before val team1 = mock(javaClass<Team>())
    before val team2 = mock(javaClass<Team>())

    before val arena = mock(javaClass<GameArena>())

    before val team1players = array<PlayerBehavior>(mock(javaClass<PlayerBehavior>()))
    before val team2players = array<PlayerBehavior>(mock(javaClass<PlayerBehavior>()))

    val FIRST_PLAYER_POSITION = #(0, 0)
    val SECOND_PLAYER_POSITION = #(0, 2)
    val SECOND_PLAYER_RELATIVE_POSITION = #(0, 0)

    before fun initGameEnvironment() {
        ifCall(arena.width).thenReturn(1)
        ifCall(arena.height).thenReturn(3)
//        ifCall(arena.goalWidth).thenReturn(1)

        ifCall(team1players[0].action(eq(FIRST_PLAYER_POSITION), isA(javaClass<Arena>(), arena))).thenReturn(DoNothing())
        ifCall(team2players[0].action(eq(SECOND_PLAYER_RELATIVE_POSITION), isA(javaClass<Arena>(), arena))).thenReturn(DoNothing())

        ifCall(team1players[0].getInitPosition(isA(javaClass<Arena>(), arena))).thenReturn(FIRST_PLAYER_POSITION)
        ifCall(team2players[0].getInitPosition(isA(javaClass<Arena>(), arena))).thenReturn(SECOND_PLAYER_RELATIVE_POSITION)

        ifCall(team1.getPlayers()).thenReturn(team1players)
        ifCall(team2.getPlayers()).thenReturn(team2players)

        var activeObjects = ArrayList<ActiveObject>()
        ifCall(arena.addActiveObjects(anyListOf(javaClass<ActiveObject>())!!)).then {
            val args = it.getArguments()!!
            activeObjects.addAll(args[0] as List<ActiveObject>)
            run{}//workaround, else doesn't compilation
        }

        ifCall(arena.get(FIRST_PLAYER_POSITION._1, FIRST_PLAYER_POSITION._2)).then {
            activeObjects[0]
        }
        ifCall(arena.get(SECOND_PLAYER_POSITION._1, SECOND_PLAYER_POSITION._2)).then {
            activeObjects[1]
        }
        ifCall(arena.get(0, 1)).then {
            activeObjects[2]
        }
    }
}