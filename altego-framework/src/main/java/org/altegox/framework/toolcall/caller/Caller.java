package org.altegox.framework.toolcall.caller;

public interface Caller<T> {

    T call(String methodName, Object... args);

    T call(String methodName, String args);

}
