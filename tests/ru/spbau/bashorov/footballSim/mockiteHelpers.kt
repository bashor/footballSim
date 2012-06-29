package ru.spbau.bashorov.footballSim.mockitoHelpers

import org.mockito.Mockito.*
import org.mockito.stubbing.OngoingStubbing
import org.mockito.Mockito
import org.mockito.verification.VerificationMode
import org.mockito.ArgumentCaptor
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock

inline fun <T> mock(clazz: Class<T>) = Mockito.mock(clazz)!!
inline fun <T> ifCall(methodCall: T): OngoingStubbing<T> = ru.spbau.bashorov.footballSim.MockitoHelper.ifCall(methodCall)!!
inline fun <T> verify(mock: T, mode: VerificationMode?) = Mockito.verify(mock, mode)!!
inline fun <T> argumentCaptor(clazz: Class<T>) = ArgumentCaptor.forClass(clazz)!!
inline fun <T> ArgumentCaptor<T>.get() = this.capture()!!
inline fun <T> anyValue() = argumentCaptor(javaClass<T>()).get()

inline fun <T> OngoingStubbing<T>.then(f: ()->T) = this.then(object: Answer<T> {
    public override fun answer(invocation: InvocationOnMock?): T? {
        return f()
    }
})