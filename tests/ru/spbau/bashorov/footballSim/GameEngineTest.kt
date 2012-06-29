package ru.spbau.bashorov.footballSim

import org.junit.Test as test
import org.junit.Before as before
import org.mockito.Mockito.*
import org.mockito.Matchers.*
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.Team
import ru.spbau.bashorov.footballSim.public.PlayerBehavior
import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.public.actions.Action
import kotlin.test.failsWith
import ru.spbau.bashorov.footballSim.IllegalArgumentException
import kotlin.test.fail
import java.util.ArrayList
import ru.spbau.bashorov.footballSim.public.ActiveObject
import org.mockito.ArgumentCaptor
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.mockito.ArgumentMatcher

public class GameEngineTest {
    test fun ConstructorWithIllegalArgsTest() {
        val team1 = mock(javaClass<Team>())
        val team2 = mock(javaClass<Team>())

        ifCall(team1.getPlayers()).thenReturn(array(mock(javaClass<PlayerBehavior>())))
        ifCall(team2.getPlayers()).thenReturn(array(mock(javaClass<PlayerBehavior>()), mock(javaClass<PlayerBehavior>())))

        failsWith<IllegalArgumentException> {
            GameEngine(team1, team2, mock(javaClass<GameArena>()), 1)
        }
    }

    test fun ConstructorTest() {
        val team1 = mock(javaClass<Team>())
        val team2 = mock(javaClass<Team>())

        var team1getPlayersCalled = false
        var team2getPlayersCalled = false
        ifCall(team1.getPlayers()).then {
            team1getPlayersCalled = true
            array<PlayerBehavior>(mock(javaClass<PlayerBehavior>()))
        }
        ifCall(team2.getPlayers()).then {
            team2getPlayersCalled = true
            array<PlayerBehavior>(mock(javaClass<PlayerBehavior>()))
        }

        val arena = mock(javaClass<GameArena>())
        ifCall(arena.width).thenReturn(1)
        ifCall(arena.height).thenReturn(1)

        var arenaAddActiveObjectsCalled = false
        ifCall(arena.addActiveObjects(anyListOf(javaClass<ActiveObject>())!!)).then {
            arenaAddActiveObjectsCalled = true
        }

//        var goalSubscribed = false
//        val a = argThat(object: ArgumentMatcher<()->Unit>(){
//            public override fun matches(argument: Any?): Boolean = true
//        })!!
//        ifCall(arena.addGoalListener(a))).then {
//            goalSubscribed = true
//        }
//
//        var outSubscribed = false
//        ifCall(arena.addOutListener(isA(javaClass<()->Unit>())!!)).then {
//            outSubscribed = true
//        }
//
//        var resetObjectsPositionsCalled = false
//        ifCall(arena._(isA(javaClass<()->Unit>())!!)).then {
//            resetObjectsPositionsCalled = true
//        }
//
//        var moveBallNearestToCalled = false
//        var moveBallNearestToCalledBeforeReset = false
//        ifCall(arena._(isA(javaClass<()->Unit>())!!)).then {
//            if (resetObjectsPositionsCalled)
//                moveBallNearestToCalled = true
//            else
//                moveBallNearestToCalledBeforeReset = true
//        }


        GameEngine(team1, team2, arena, 1)

        assertTrue(team1getPlayersCalled)
        assertTrue(team2getPlayersCalled)
        assertTrue(arenaAddActiveObjectsCalled)
//        assertTrue(goalSubscribed)
//        assertTrue(outSubscribed)
//        assertTrue(resetObjectsPositionsCalled)
//        assertTrue(moveBallNearestToCalled)
//        assertFalse(moveBallNearestToCalledBeforeReset)
    }
    //duration
    //printer call
    //sleep?
}