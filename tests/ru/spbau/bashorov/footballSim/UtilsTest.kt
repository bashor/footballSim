package ru.spbau.bashorov.footballSim

import kotlin.test.*
import org.junit.Before as before
import org.junit.Test as test
import ru.spbau.bashorov.footballSim.mockitoHelpers.*
import ru.spbau.bashorov.footballSim.public.Arena
import ru.spbau.bashorov.footballSim.utils.invertCoordinates

test class UtilsTest {
    val WIDTH = 11
    val HEIGHT = 17
    val LEFT_TOP_CORNER = #(0, 0)
    val RIGHT_TOP_CORNER = #(WIDTH - 1, 0)
    val LEFT_BOTTOM_CORNER = #(0, HEIGHT - 1)
    val RIGHT_BOTTOM_CORNER = #(WIDTH - 1, HEIGHT - 1)
    val CENTER = #(WIDTH / 2,HEIGHT / 2)

    before var mockedArena: Arena = mock(javaClass<Arena>())

    before fun initArena() {
        ifCall(mockedArena.width).thenReturn(WIDTH)
        ifCall(mockedArena.height).thenReturn(HEIGHT)
    }

    test fun invertCoordinatesTest() {
        expect(RIGHT_BOTTOM_CORNER) {
            invertCoordinates(mockedArena, LEFT_TOP_CORNER)
        }

        expect(LEFT_TOP_CORNER) {
            invertCoordinates(mockedArena, RIGHT_BOTTOM_CORNER)
        }

        expect(LEFT_BOTTOM_CORNER) {
            invertCoordinates(mockedArena, RIGHT_TOP_CORNER)
        }

        expect(RIGHT_TOP_CORNER) {
            invertCoordinates(mockedArena, LEFT_BOTTOM_CORNER)
        }

        expect(CENTER) {
            invertCoordinates(mockedArena, CENTER)
        }
    }
}