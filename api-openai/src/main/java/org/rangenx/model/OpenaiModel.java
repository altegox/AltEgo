package org.rangenx.model;

import org.rangenx.framework.api.LangModel;
import org.rangenx.framework.api.request.DefaultRequest;

public class OpenaiModel extends LangModel {

    DefaultRequest requestParam;

    public OpenaiModel() {
        super();
    }

    public DefaultRequest requestParam() {
        return requestParam;
    }

    public OpenaiModel(String baseUrl, String apiKey, String modelName, boolean stream, DefaultRequest requestParam) {
        super(baseUrl, apiKey, modelName, stream);
        this.requestParam = requestParam;
    }

    public static class Builder extends LangModel.Builder {
        private String baseUrl;
        private String apiKey;
        private String modelName;
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

        public Builder requestParam(DefaultRequest requestParam) {
            this.requestParam = requestParam;
            return this;
        }

        public OpenaiModel build() {
            return new OpenaiModel(baseUrl, apiKey, modelName, stream, requestParam);
        }
    }

    public static OpenaiModel.Builder builder() {
        return new Builder();
    }

}
