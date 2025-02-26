package org.altegox.toolcall;

import org.altego.framework.annotation.Component;

@Component
public class TestConstruct2 {

    private String id = "1";

    private String content = "test ioc";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
