package ru.spbau.bashorov.footballSim.public

import ru.spbau.bashorov.footballSim.GameObject
import ru.spbau.bashorov.footballSim.GamePlayer
import ru.spbau.bashorov.footballSim.Ball

public abstract class ReadOnlyObject(private val obj: GameObject): GameObject {
    public override val sym: Char = obj.sym
}

public class ReadOnlyBall(ball: Ball) : ReadOnlyObject(ball)

public open class Player(realPlayer: GamePlayer): ReadOnlyObject(realPlayer)
public class PartnerPlayer(realPlayer: GamePlayer): Player(realPlayer)
public class OpponentPlayer(realPlayer: GamePlayer): Player(realPlayer)