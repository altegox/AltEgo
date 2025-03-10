package org.altegox.framework.api;

import org.altegox.framework.toolcall.ToolEntity;

import java.util.List;
import java.util.function.Function;

public class LangModel {
    private final String baseUrl;
    private final String apiKey;
    private final String modelName;
    private final List<ToolEntity> tools;
    private final boolean stream;
    private final LangModel reasonerModel;
    private final LangModel generateModel;

    public LangModel() {
        this.baseUrl = null;
        this.apiKey = null;
        this.modelName = null;
        this.tools = null;
        this.stream = false;
        this.reasonerModel = null;
        this.generateModel = null;
    }

    protected LangModel(Builder<?> builder) {
        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.modelName = builder.modelName;
        this.tools = builder.tools;
        this.stream = builder.stream;

        // 继承主模型的未设置字段
        this.reasonerModel = inheritUnsetFields(builder.reasonerModel, this);
        this.generateModel = inheritUnsetFields(builder.generateModel, this);
    }

    private LangModel inheritUnsetFields(LangModel child, LangModel parent) {
        if (child == null) return parent; // 若子模型未定义，直接返回主模型
        return new Builder<>()
                .baseUrl(child.baseUrl != null ? child.baseUrl : parent.baseUrl)
                .apiKey(child.apiKey != null ? child.apiKey : parent.apiKey)
                .modelName(child.modelName != null ? child.modelName : parent.modelName)
                .tools(child.tools != null ? child.tools : parent.tools)
                .stream(child.stream)  // 流式处理由子模型自己决定
                .build();
    }

    public String getBaseUrl() { return baseUrl; }
    public String getApiKey() { return apiKey; }
    public String getModelName() { return modelName; }
    public List<ToolEntity> getTools() { return tools; }
    public boolean isStream() { return stream; }
    public LangModel getReasonerModel() { return reasonerModel; }
    public LangModel getGenerateModel() { return generateModel; }

    @Override
    public String toString() {
        return "LangModel {" +
                "baseUrl='" + baseUrl + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", modelName='" + modelName + '\'' +
                ", tools=" + tools +
                ", stream=" + stream +
                ", reasonerModel=" + (reasonerModel != null ? reasonerModel.getModelName() : "null") +
                ", generateModel=" + (generateModel != null ? generateModel.getModelName() : "null") +
                '}';
    }

    public static Builder<?> builder() {
        return new Builder<>();
    }

    public static class Builder<T extends Builder<T>> {
        private String baseUrl;
        private String apiKey;
        private String modelName;
        private List<ToolEntity> tools;
        private boolean stream;
        private LangModel reasonerModel;
        private LangModel generateModel;

        public T baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return self();
        }

        public T apiKey(String apiKey) {
            this.apiKey = apiKey;
            return self();
        }

        public T modelName(String modelName) {
            this.modelName = modelName;
            return self();
        }

        public T tools(List<ToolEntity> tools) {
            this.tools = tools;
            return self();
        }

        public T stream(boolean stream) {
            this.stream = stream;
            return self();
        }

        public T reasoner(Function<Builder<?>, Builder<?>> reasonerConfig) {
            this.reasonerModel = reasonerConfig.apply(new Builder<>()).build();
            return self();
        }

        public T generate(Function<Builder<?>, Builder<?>> generateConfig) {
            this.generateModel = generateConfig.apply(new Builder<>()).build();
            return self();
        }

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public LangModel build() {
            return new LangModel(this);
        }
    }
}