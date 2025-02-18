package org.rangenx.framework.toolcall.caller;

public interface Caller<T> {

    T call(String methodName, Object... args);

}
