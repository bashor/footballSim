package ru.spbau.bashorov.footballSim.mockitoHelpers

import org.mockito.Mockito.*
import org.mockito.stubbing.OngoingStubbing
import org.mockito.Mockito
import org.mockito.verification.VerificationMode

fun <T> ifCall(methodCall: T): OngoingStubbing<T> = ru.spbau.bashorov.footballSim.MockitoHelper.ifCall(methodCall)!!
fun <T> mock(clazz: Class<T>) = Mockito.mock(clazz)!!

fun <T> verify(mock: T, mode: VerificationMode?) = Mockito.verify(mock, mode)!!