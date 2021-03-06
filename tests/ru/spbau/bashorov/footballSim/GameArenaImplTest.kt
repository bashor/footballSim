package ru.spbau.bashorov.footballSim

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.failsWith
import org.junit.Before as before
import org.junit.Test as test
import org.mockito.Mockito.*
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.ActiveObject
import ru.spbau.bashorov.footballSim.public.exceptions.CanNotMoveToPositionException
import ru.spbau.bashorov.footballSim.public.gameObjects.Free

public class GameArenaImplTest {
    val WIDTH = 11
    val HEIGHT = 17
    val GOAL_WIDTH = 7

    before val arena = GameArenaImpl(HEIGHT, WIDTH, GOAL_WIDTH)

    test fun constructorTest() {
        assertEquals(arena.width, WIDTH)
        assertEquals(arena.height, HEIGHT)
        assertEquals(arena.goalWidth, GOAL_WIDTH)
    }

    test fun constructorWithIllegalParamsTest() {
        failsWith<IllegalArgumentException> {
            GameArenaImpl(2, WIDTH, GOAL_WIDTH)
        }

        failsWith<IllegalArgumentException> {
            GameArenaImpl(-1, WIDTH, GOAL_WIDTH)
        }

        failsWith<IllegalArgumentException> {
            GameArenaImpl(HEIGHT, 2, GOAL_WIDTH)
        }

        failsWith<IllegalArgumentException> {
            GameArenaImpl(HEIGHT, -1, GOAL_WIDTH)
        }

        failsWith<IllegalArgumentException> {
            GameArenaImpl(HEIGHT, WIDTH, WIDTH + 1)
        }

        failsWith<IllegalArgumentException> {
            GameArenaImpl(HEIGHT, WIDTH, 2)
        }
    }

    test fun addActiveObjectsWithOutBallTest() {
        failsWith<BallNotFoundException> {
            val activeObjects = arrayList<ActiveObject>(mock(javaClass<ActiveObject>()), mock(javaClass<ActiveObject>()))
            arena.addActiveObjects(activeObjects)
        }
    }

    val activeObjects = arrayList<ActiveObject>(
            mock(javaClass<ActiveObject>()),
            mock(javaClass<ActiveObject>()),
            mock(javaClass<ActiveObject>()),
            Ball())

    val positions = arrayList(Pair(0, 0), Pair(1, 0), Pair(0, HEIGHT - 1), Pair(WIDTH / 2, HEIGHT / 2))

    fun initArena() {
        for (i in 0..activeObjects.size - 2) {
            ifCall(activeObjects[i].getInitPosition(arena)).thenReturn(positions[i]);
        }

        arena.addActiveObjects(activeObjects)
        arena.resetObjectsPositions()
    }

    test fun resetObjectsPositionsTest() {
        initArena()

        verify(activeObjects[0], atLeastOnce()).getInitPosition(arena);
        verify(activeObjects[1], atLeastOnce()).getInitPosition(arena);

        assertEquals(arena[positions[0]], activeObjects[0])
        assertEquals(arena[positions[1]], activeObjects[1])

        for(i in 0..arena.width - 1) {
            for(j in 0..arena.height - 1) {
                if (!positions.contains(Pair(i, j))) {
                    assertTrue("arena[$i, $j] is not Free"){arena[i, j] is Free}
                }
            }
        }
    }

    test fun callMoveWithIllegalParams() {
        initArena()

        failsWith<IllegalArgumentException> {
            arena.move(activeObjects[0], Pair(-1, 0))
        }

        failsWith<IllegalArgumentException> {
            arena.move(activeObjects[0], Pair(0, -1))
        }

        failsWith<IllegalArgumentException> {
            arena.move(activeObjects[0], Pair(WIDTH, -1))
        }
    }

    test fun expectCanNotMove() {
        initArena()

        // not free
        failsWith<CanNotMoveToPositionException> {
            arena.move(activeObjects[0], positions[1])
        }

        failsWith<CanNotMoveToPositionException> {
            arena.move(activeObjects[0], positions[2])
        }
    }

    test fun moveBallNearestTo() {
        initArena()

        val TO = 1
        arena.moveBallNearestTo{it == activeObjects[TO]}

        assertTrue{arena[positions[TO].first, positions[TO].second + 1] is Ball};
    }

    fun ballGoToBesideGoal(lastMovePosition: Pair<Int, Int>, goalExpected: Boolean) {
        initArena()

        val TO = 1
        arena.moveBallNearestTo{it == activeObjects[TO]}
        var goalCalled = false
        arena.addGoalListener{ goalCalled = true }
        var outCalled = false
        arena.addOutListener{ outCalled = true }

        assertTrue{activeObjects.last is Ball}

        arena.move(activeObjects.last!!, Pair(2, 0))
        arena.move(activeObjects.last!!, lastMovePosition)

        assertEquals(goalCalled, goalExpected)
        val outEpected = !goalExpected
        assertEquals(outCalled, outEpected)
    }

    test fun goalNotification() {
        ballGoToBesideGoal(Pair(2, -1), true)
    }

    test fun goalFalseNotification() {
        ballGoToBesideGoal(Pair(1, -1), false)
    }
}