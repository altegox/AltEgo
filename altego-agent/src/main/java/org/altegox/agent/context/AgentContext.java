package org.altegox.agent.context;

import java.util.concurrent.ConcurrentHashMap;

public class AgentContext {
    private AgentContext() {
    }

    private static class Holder {
        static final AgentContext INSTANCE = new AgentContext();
    }

    public static AgentContext getContext() {
        return Holder.INSTANCE;
    }

    private final ConcurrentHashMap<String, AgentConfig> agentConfigMap = new ConcurrentHashMap<>();

    public AgentConfig getAgentConfigByGroup(String group) {
        return agentConfigMap.get(group);
    }

    public void setAgentConfigByGroup(String group, AgentConfig agentConfig) {
        agentConfigMap.put(group, agentConfig);
    }

}


