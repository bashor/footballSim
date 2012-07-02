package ru.spbau.bashorov.footballSim.mockitoHelpers

import org.mockito.ArgumentCaptor
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.stubbing.OngoingStubbing
import org.mockito.verification.VerificationMode

inline fun <T> mock(clazz: Class<T>) = Mockito.mock(clazz)!!
inline fun <T> ifCall(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!
inline fun <T> verify(mock: T, mode: VerificationMode?) = Mockito.verify(mock, mode)!!
inline fun <T> argumentCaptor(clazz: Class<T>) = ArgumentCaptor.forClass(clazz)!!
inline fun <T> ArgumentCaptor<T>.get() = this.capture()!!

inline fun <T> OngoingStubbing<T>.thenSet(out var f: Boolean) = this.then(object: Answer<T> {
    public override fun answer(invocation: InvocationOnMock?): T? {
        f = true
        return null
    }
})

inline fun <T> OngoingStubbing<T>.then(f: ()->T) = this.then(object: Answer<T> {
    public override fun answer(invocation: InvocationOnMock?): T? {
        return f()
    }
})

inline fun <T> OngoingStubbing<T>.then(f: (inv: InvocationOnMock)->T) = this.then(object: Answer<T> {
    public override fun answer(invocation: InvocationOnMock?): T? {
        return f(invocation!!)
    }
})

inline fun <T> isA(clazz: Class<T>, ret: T): T {
    Matchers.isA(clazz)
    return ret
}

inline fun <T> eq(a: T): T {
    Matchers.eq(a)
    return a
}