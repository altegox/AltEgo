package org.altegox;

import org.altegox.framework.api.LangModel;

public class BaseAgent extends LangModel {

    private final String name;

    private final String prompt;

    private BaseAgent(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.prompt = builder.prompt;
    }

    public String getName() {
        return name;
    }

    public String getPrompt() {
        return prompt;
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

}