package org.rangenx.toolcall.caller;

public interface Caller<T> {

    T call(String methodName, Object... args);

}
