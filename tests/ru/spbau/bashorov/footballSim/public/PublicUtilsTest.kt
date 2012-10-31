package ru.spbau.bashorov.footballSim.public

import kotlin.test.*
import org.junit.Before as before
import org.junit.Test as test
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.gameObjects.Free
import ru.spbau.bashorov.footballSim.public.utils.*

public class PublicUtilsTest {
    val position1 = Pair(1, 4)
    val position2 = Pair(2, 2)

    test fun calcDistanceBetweenPositions() {
        val distance = 2

        expect(distance) {
            position1.calcDistanceTo(position2)
        }
        expect(distance) {
            position2.calcDistanceTo(position1)
        }
    }

    test fun positionsPlus() {
        val sum = Pair(3, 6)

        expect(sum) {
            position1 + position2
        }
        expect(sum) {
            position2 + position1
        }
    }

    test fun positionsMinus() {
        expect(Pair(-1, 2)) {
            position1 - position2
        }
        expect(Pair(1, -2)) {
            position2 - position1
        }
    }

    test fun positionsEqualTest() {
        assertFalse(position1 == position2)
        assertEquals(position1, Pair(1, 4))
        assertEquals(position2, Pair(2, 2))
    }

    test fun positionsAchievableTester() {
        assertFalse(position1 isAchievableFrom position2)
        assertTrue(position1 isAchievableFrom Pair(1, 3))
        assertTrue(position2 isAchievableFrom Pair(1, 2))
    }

    test fun positionsValidator() {
        val arena = mock(javaClass<Arena>())
        ifCall(arena.width).thenReturn(3)
        ifCall(arena.height).thenReturn(3)

        assertTrue(position2 isValidCellOn arena)
        assertFalse(position1 isValidCellOn arena)
    }

    test fun stepTo() {
        val arena = mock(javaClass<Arena>())
        ifCall(arena.width).thenReturn(3)
        ifCall(arena.height).thenReturn(3)
        ifCall(arena.get(isA(javaClass<Pair<Int, Int>>(), Pair(0, 0)))).thenReturn(Free())

        expect(Pair(1, 2)){
            stepTo(position2, position1, arena)
        }
    }
}