package org.altegox.api;

import org.altegox.framework.api.LangModel;
import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.toolcall.ToolEntity;

import java.util.List;

public class OpenaiModel extends LangModel {

    private DefaultRequest requestParam;

    public OpenaiModel() {
        super();
    }

    public DefaultRequest requestParam() {
        return requestParam;
    }

    public OpenaiModel(String baseUrl, String apiKey, String modelName, boolean stream) {
        super(baseUrl, apiKey, modelName, stream);
    }

    public OpenaiModel(String baseUrl, String apiKey, String modelName, List<ToolEntity> tools, boolean stream) {
        super(baseUrl, apiKey, modelName, tools, stream);
    }

    public OpenaiModel(String baseUrl, String apiKey, String modelName, List<ToolEntity> tools, boolean stream,
                       DefaultRequest requestParam) {
        super(baseUrl, apiKey, modelName, tools, stream);
        this.requestParam = requestParam;
    }

    public static class Builder extends LangModel.Builder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private List<ToolEntity> tools;
        private boolean stream;
        private DefaultRequest requestParam;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder tools(List<ToolEntity> tools) {
            this.tools = tools;
            return this;
        }

        public Builder requestParam(DefaultRequest requestParam) {
            this.requestParam = requestParam;
            return this;
        }

        public OpenaiModel build() {
            return new OpenaiModel(baseUrl, apiKey, modelName, tools, stream, requestParam);
        }
    }

    public static OpenaiModel.Builder builder() {
        return new Builder();
    }

}
