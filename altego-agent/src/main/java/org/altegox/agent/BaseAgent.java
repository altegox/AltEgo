package org.altegox.agent;

import org.altegox.api.OpenaiClient;
import org.altegox.api.OpenaiModel;
import org.altegox.framework.api.LangModel;
import org.altegox.framework.toolcall.ToolManager;

public class BaseAgent extends LangModel {

    protected static String DEFAULT_SYSTEM_MESSAGE = """
            你是一个可以做到任何事情的的AI助手，你可以随意使用提供的Tool/Function完成用户交给你的任务，并且可以连续多次调用Tool。
            在接到任务时候，你应该将其分解成多个子问题并逐个去解决，如果其中有子任务需要其他Agent帮你，你可以调用CreateAgent工具创建另一个Agent帮助你完成某个任务。
            """;

    private final String name;

    private final String prompt;

    public BaseAgent(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.prompt = builder.prompt;
        this.tools = ToolManager.getInstance().getToolsByGroup(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends LangModel.Builder<Builder> {
        private String name;
        private String prompt;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        @Override
        public BaseAgent build() {
            return new BaseAgent(this);
        }
    }

    public String execute() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(this.getBaseUrl())
                .apiKey(this.getApiKey())
                .modelName(this.getModelName())
                .tools(this.getTools())
                .stream(this.isStream())
                .build();

        OpenaiClient openaiClient = OpenaiClient.create(model);
        return openaiClient.generate(this.prompt);
    }

}
