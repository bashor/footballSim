package ru.spbau.bashorov.footballSim.public.exceptions

public open class PlayerBehaviorException(message: String = "") : RuntimeException(message)

public class ObjectNotFoundException(message: String = "Object not found.") : PlayerBehaviorException(message)
public class CanNotMoveToPositionException(message: String = "Can not move to position.") : PlayerBehaviorException(message)
public class UnknownActionException(message: String = "Unknown action.") : PlayerBehaviorException(message)
public class AchievablePositionNotFoundException(message: String = "Achievable position not found.") : PlayerBehaviorException(message)
public class IllegalArgumentException(message: String = "") : PlayerBehaviorException(message)
public class UnknownObjectException(message: String = "Unknown object.") : PlayerBehaviorException(message)