package ru.spbau.bashorov.footballSim;

import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.when;

final public class MockitoHelper {
    public static <T> OngoingStubbing<T> ifCall(final T methodCall) {
        return when(methodCall);
    }
}
