package org.altegox.agent.context;

public record AgentConfig(String agentName, String baseUrl, String apiKey, String modelName) {

    public static AgentConfig of(String agentName, String baseUrl, String apiKey, String modelName) {
        return new AgentConfig(agentName, baseUrl, apiKey, modelName);
    }

}