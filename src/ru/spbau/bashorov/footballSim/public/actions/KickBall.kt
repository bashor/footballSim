package ru.spbau.bashorov.footballSim.public

public class KickBall(val direction: Direction): Action

public enum class Direction {
    NOWHERE;
    NORTH; SOUTH; WEST; EAST;
    NORTHWEST; NORTHEAST; SOUTHWEST; SOUTHEAST;
}