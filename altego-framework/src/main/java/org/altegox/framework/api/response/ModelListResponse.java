package org.altegox.framework.api.response;

import java.util.List;

public class ModelListResponse {

    private List<Model> data;

    private String object;

    // Getters and Setters
    public List<Model> getData() {
        return data;
    }

    public void setData(List<Model> data) {
        this.data = data;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
