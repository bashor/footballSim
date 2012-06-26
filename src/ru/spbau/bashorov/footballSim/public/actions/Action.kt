package ru.spbau.bashorov.footballSim.public.actions

import ru.spbau.bashorov.footballSim.public.Arena

public trait Action {
    internal fun invert(arena: Arena): Action
}