package org.altego.framework.toolcall.caller;

public interface Caller<T> {

    T call(String methodName, Object... args);

}
