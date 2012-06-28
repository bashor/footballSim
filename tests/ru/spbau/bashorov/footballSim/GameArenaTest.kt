package ru.spbau.bashorov.footballSim

import org.junit.Test as test
import org.junit.Before as before
import org.mockito.Mockito.*
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import kotlin.test.assertEquals
import ru.spbau.bashorov.footballSim.public.ActiveObject
import java.util.ArrayList

class GameArenaTest {
    val WIDTH = 11
    val HEIGHT = 17
    val GOAL_WIDTH = 7

    before val arena = GameArena(HEIGHT, WIDTH, GOAL_WIDTH)

    test fun constructorTest() {
        assertEquals(arena.width, WIDTH)
        assertEquals(arena.height, HEIGHT)
        assertEquals(arena.goalWidth, GOAL_WIDTH)
    }

    test (expected=javaClass<BallNotFoundException>())
    fun addActiveObjectsWithOutBallTest() {
        val activeObjects = arrayList<ActiveObject>(mock(javaClass<ActiveObject>()), mock(javaClass<ActiveObject>()))
        arena.addActiveObjects(activeObjects)
    }

    test fun resetObjectsPositionsTest() {
        val activeObjects = arrayList<ActiveObject>(
                mock(javaClass<ActiveObject>()),
                mock(javaClass<ActiveObject>()),
                Ball())

        val positions = array(#(0, 0), #(0, HEIGHT - 1))

        ifCall(activeObjects[0].getInitPosition(arena)).thenReturn(positions[0]);
        ifCall(activeObjects[1].getInitPosition(arena)).thenReturn(positions[1]);

        arena.addActiveObjects(activeObjects)

        arena.resetObjectsPositions()

        verify(activeObjects[0], atLeastOnce()).getInitPosition(arena);
        verify(activeObjects[1], atLeastOnce()).getInitPosition(arena);

        assertEquals(arena[positions[0]], activeObjects[0])
        assertEquals(arena[positions[1]], activeObjects[1])
    }

}