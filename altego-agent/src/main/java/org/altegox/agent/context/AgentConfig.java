package org.altegox.agent.context;

public class AgentConfig {

    private String baseUrl;
    private String apiKey;
    private String modelName;

    private AgentConfig(String baseUrl, String apiKey, String modelName) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.modelName = modelName;
    }

    public AgentConfig() {
    }

    public static AgentConfig of(String baseUrl, String apiKey, String modelName) {
        return new AgentConfig(baseUrl, apiKey, modelName);
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

}