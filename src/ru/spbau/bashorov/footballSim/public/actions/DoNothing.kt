package ru.spbau.bashorov.footballSim.public.actions

import ru.spbau.bashorov.footballSim.public.Arena

public class DoNothing: Action {
    internal override fun invert(arena: Arena): Action = this
}