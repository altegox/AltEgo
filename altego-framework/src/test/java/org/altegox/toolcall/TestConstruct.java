package org.altegox.toolcall;

import org.altegox.framework.annotation.Component;

@Component
public class TestConstruct {

    private String id;

    private String content;

    public TestConstruct(TestConstruct2 testConstruct2) {
        this.id = testConstruct2.getId();
        this.content = testConstruct2.getContent();
    }

    @Override
    public String toString() {
        return "TestConstruct {" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
