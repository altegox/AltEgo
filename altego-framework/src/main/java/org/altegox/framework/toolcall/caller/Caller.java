package org.altegox.framework.toolcall.caller;

import org.altegox.framework.model.LangModel;

public interface Caller<T> {

    T call(String methodName, Object... args);

    T call(LangModel model, String methodName, String args);

}
