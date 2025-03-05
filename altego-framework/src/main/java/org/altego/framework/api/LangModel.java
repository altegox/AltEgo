package org.altego.framework.api;

import java.util.function.Function;

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

    public LangModel() {}

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public boolean isStream() { return stream; }
    public void setStream(boolean stream) { this.stream = stream; }

    public LangModel getReasonerModel() { return reasonerModel; }
    public void setReasonerModel(LangModel reasonerModel) { this.reasonerModel = reasonerModel; }
    public LangModel getGenerateModel() { return generateModel; }
    public void setGenerateModel(LangModel generateModel) { this.generateModel = generateModel; }

    @Override
    public String toString() {
        return "LangModel {" +
                "baseUrl='" + baseUrl + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", modelName='" + modelName + '\'' +
                ", stream=" + stream +
                ", reasonerModel=" + (reasonerModel != null ? reasonerModel.getModelName() : "null") +
                ", generateModel=" + (generateModel != null ? generateModel.getModelName() : "null") +
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

        public Builder reasoner(LangModel reasonerModel) {
            this.reasonerModel = reasonerModel;
            return this;
        }

        public Builder reasoner(Function<LangModel, LangModel> reasonerModel) {
            this.reasonerModel = reasonerModel.apply(new LangModel());
            return this;
        }

        public Builder generate(LangModel generateModel) {
            this.generateModel = generateModel;
            return this;
        }

        public Builder generate(Function<LangModel, LangModel> generateModel) {
            this.generateModel = generateModel.apply(new LangModel());
            return this;
        }

        public LangModel build() {
            return new LangModel(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}