package ru.spbau.bashorov.footballSim.public.gameObjects

import ru.spbau.bashorov.footballSim.Ball
import ru.spbau.bashorov.footballSim.GamePlayer
import ru.spbau.bashorov.footballSim.public.GameObject
import ru.spbau.bashorov.footballSim.public.PlayerBehavior

public abstract class ReadOnlyObject(protected val obj: GameObject): GameObject {
    public override val sym: Char = obj.sym
}

public class ReadOnlyBall(ball: Ball) : ReadOnlyObject(ball)

public open class Player(realPlayer: GamePlayer): ReadOnlyObject(realPlayer)
public class OpponentPlayer(realPlayer: GamePlayer): Player(realPlayer)
public class PartnerPlayer(realPlayer: GamePlayer): Player(realPlayer) {
    public val playerBehavior: PlayerBehavior = (obj as GamePlayer).playerBehavior
}
