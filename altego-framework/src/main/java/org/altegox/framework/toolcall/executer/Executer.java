package org.altegox.framework.toolcall.executer;

public interface Executer {

    String exec(String args);

    boolean exec(String args, String outPath);

}
