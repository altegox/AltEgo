package org.altego.framework.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefaultRequest {

    // 用户id
    private String user;

    // 模型名称
    private String model;

    // 聊天消息列表
    private List<Message> messages;

    // 可选参数：生成文本的最大 token 数量
    @SerializedName("max_tokens")
    private Integer maxTokens;

    // 可选参数：控制生成文本的随机性，0 表示完全确定性
    private Double temperature;

    // 可选参数： nucleus sampling 参数，通常取值 0-1
    @SerializedName("top_p")
    private Double topP;

    // 可选参数：一次请求返回多少个候选结果
    private Integer n;

    // 可选参数：是否以流式返回结果
    private Boolean stream;

    // 可选参数：停止生成的标识符，可以为 null
    private String stop;

    // 可选参数：生成时对话重复内容的惩罚，默认 0
    @SerializedName("presence_penalty")
    private Double presencePenalty;

    // 可选参数：生成时对话重复使用同一 token 的惩罚，默认 0
    @SerializedName("frequency_penalty")
    private Double frequencyPenalty;

    // 可选参数：修改指定标记出现在完成中的可能性
    @SerializedName("logit_bias")
    private String logitBias;

    public DefaultRequest() {
    }

    public DefaultRequest(String model, String user, List<Message> messages, Integer maxTokens, Double temperature,
                          Double topP, Integer n, Boolean stream, String stop,
                          Double presencePenalty, Double frequencyPenalty, String logitBias) {
        this.model = model;
        this.user = user;
        this.messages = messages;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.stream = stream;
        this.stop = stop;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.logitBias = logitBias;
    }

    // Getter 和 Setter 方法

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Boolean isStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public Double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public String getLogitBias() {
        return logitBias;
    }

    public void setLogitBias(String logitBias) {
        this.logitBias = logitBias;
    }

    @Override
    public String toString() {
        return "ChatGPTRequest {" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                ", user='" + user + '\'' +
                ", maxTokens=" + maxTokens +
                ", temperature=" + temperature +
                ", topP=" + topP +
                ", n=" + n +
                ", stream=" + stream +
                ", stop='" + stop + '\'' +
                ", presencePenalty=" + presencePenalty +
                ", frequencyPenalty=" + frequencyPenalty +
                ", logitBias='" + logitBias + '\'' +
                '}';
    }

    public static class Builder {
        private String model;
        private String user;
        private List<Message> messages;
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private Integer n;
        private Boolean stream;
        private String stop;
        private Double presencePenalty;
        private Double frequencyPenalty;
        private String logitBias;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder n(Integer n) {
            this.n = n;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder stop(String stop) {
            this.stop = stop;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder logitBias(String logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public DefaultRequest build() {
            return new DefaultRequest(model, user, messages, maxTokens, temperature, topP, n, stream, stop,
                    presencePenalty, frequencyPenalty, logitBias);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}
