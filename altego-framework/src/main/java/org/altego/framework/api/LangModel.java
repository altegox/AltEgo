package org.altego.framework.api;

import java.util.function.Supplier;

public class LangModel {

    private String baseUrl;
    private String apiKey;
    private String modelName;
    private boolean stream;
    private LangModel reasonerModel;
    private LangModel generateModel;

    private LangModel(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.modelName = builder.modelName;
        this.stream = builder.stream;
        this.reasonerModel = builder.reasonerModel;
        this.generateModel = builder.generateModel;
    }

    public LangModel(String baseUrl, String apiKey, String modelName, boolean stream) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.stream = stream;
    }

    public LangModel() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isStream() {
        return stream;
    }

    public LangModel getReasonerModel() {
        return reasonerModel;
    }

    public LangModel getGenerateModel() {
        return generateModel;
    }

    @Override
    public String toString() {
        return "LangModel {" +
                "baseUrl='" + baseUrl + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", modelName='" + modelName + '\'' +
                ", stream=" + stream +
                ", reasonerModel=" + (reasonerModel != null ? reasonerModel.getClass().getSimpleName() +
                " (" + reasonerModel.getModelName() + ")" : "null") +
                ", generateModel=" + (generateModel != null ? generateModel.getClass().getSimpleName() +
                " (" + generateModel.getModelName() + ")" : "null") +
                '}';
    }

    public static class Builder {

        private String baseUrl;
        private String apiKey;
        private String modelName;
        private boolean stream;
        private LangModel reasonerModel;
        private LangModel generateModel;

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

        public Builder combine(LangModel reasonerModel, LangModel generateModel) {
            this.reasonerModel = reasonerModel;
            this.generateModel = generateModel;

            if (this.reasonerModel != null && this.generateModel != null) {
                applyConfig(this.reasonerModel);
                applyConfig(this.generateModel);
            }
            return this;
        }

        private void applyConfig(LangModel model) {
            model.baseUrl = this.baseUrl == null ? model.baseUrl : this.baseUrl;
            model.apiKey = this.apiKey == null ? model.apiKey : this.apiKey;
            model.stream = this.stream;
        }

        public LangModel build() {
            return new LangModel(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }
}