package org.altegox.agent.context;

public class AgentContext {
    private AgentContext() {
    }

    private static class Holder {
        static final AgentContext INSTANCE = new AgentContext();
    }

    public static AgentContext getContext() {
        return Holder.INSTANCE;
    }

    private AgentConfig agentConfig = null;

    public AgentConfig getAgentConfig() {
        return agentConfig;
    }

    public void initAgentConfig(String baseUrl, String apiKey, String modelName) {
        agentConfig = AgentConfig.of(baseUrl, apiKey, modelName);
    }

    public String getApiKey() {
        return agentConfig.getApiKey();
    }

    public String getBaseUrl() {
        return agentConfig.getBaseUrl();
    }

    public String getModelName() {
        return agentConfig.getModelName();
    }

}


