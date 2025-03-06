package org.altegox.framework.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefaultChatResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("object")
    private String object;

    @SerializedName("created")
    private long created;

    @SerializedName("model")
    private String model;

    @SerializedName("system_fingerprint")
    private String systemFingerprint;

    @SerializedName("choices")
    private List<Choice> choices;

    @SerializedName("usage")
    private Usage usage;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public void setSystemFingerprint(String systemFingerprint) {
        this.systemFingerprint = systemFingerprint;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "DefaultChatResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", systemFingerprint='" + systemFingerprint + '\'' +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }

    public static class Choice {
        @SerializedName("index")
        private int index;

        @SerializedName("finish_reason")
        private String finishReason;

        @SerializedName("logprobs")
        private Object logprobs; // 可以是 null

        @SerializedName("message")
        private Message message;

        // Getters and Setters
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public Object getLogprobs() {
            return logprobs;
        }

        public void setLogprobs(Object logprobs) {
            this.logprobs = logprobs;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Choice{" +
                    "index=" + index +
                    ", finishReason='" + finishReason + '\'' +
                    ", logprobs=" + logprobs +
                    ", message=" + message +
                    '}';
        }

    }

    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        @SerializedName("refusal")
        private String refusal; // 可能为 null

        @SerializedName("audio")
        private String audio; // 可能为 null

        @SerializedName("function_call")
        private String functionCall; // 可能为 null

        @SerializedName("tool_calls")
        private List<ToolCall> toolCalls; // 可能为 null

        // Getters and Setters
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getRefusal() {
            return refusal;
        }

        public void setRefusal(String refusal) {
            this.refusal = refusal;
        }

        public Object getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public Object getFunctionCall() {
            return functionCall;
        }

        public void setFunctionCall(String functionCall) {
            this.functionCall = functionCall;
        }

        public List<ToolCall> getToolCalls() {
            return toolCalls;
        }

        public void setToolCalls(List<ToolCall> toolCalls) {
            this.toolCalls = toolCalls;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "role='" + role + '\'' +
                    ", content='" + content + '\'' +
                    ", refusal=" + refusal +
                    ", audio=" + audio +
                    ", functionCall=" + functionCall +
                    ", toolCalls=" + toolCalls +
                    '}';
        }
    }

    public static class ToolCall {
        @SerializedName("id")
        private String id;

        @SerializedName("function")
        private FunctionCall function;

        @SerializedName("type")
        private String type;

        @SerializedName("index")
        private int index;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FunctionCall getFunction() {
            return function;
        }

        public void setFunction(FunctionCall function) {
            this.function = function;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "ToolCall{" +
                    "id='" + id + '\'' +
                    ", function=" + function +
                    ", type='" + type + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    public static class FunctionCall {
        @SerializedName("name")
        private String name;

        @SerializedName("arguments")
        private String arguments;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArguments() {
            return arguments;
        }

        public void setArguments(String arguments) {
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return "FunctionCall{" +
                    "name='" + name + '\'' +
                    ", arguments='" + arguments + '\'' +
                    '}';
        }
    }

    public static class Usage {
        @SerializedName("prompt_tokens")
        private int promptTokens;

        @SerializedName("completion_tokens")
        private int completionTokens;

        @SerializedName("total_tokens")
        private int totalTokens;

        @SerializedName("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;

        @SerializedName("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;

        // Getters and Setters
        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }

        public CompletionTokensDetails getCompletionTokensDetails() {
            return completionTokensDetails;
        }

        public void setCompletionTokensDetails(CompletionTokensDetails completionTokensDetails) {
            this.completionTokensDetails = completionTokensDetails;
        }

        public PromptTokensDetails getPromptTokensDetails() {
            return promptTokensDetails;
        }

        public void setPromptTokensDetails(PromptTokensDetails promptTokensDetails) {
            this.promptTokensDetails = promptTokensDetails;
        }

        @Override
        public String toString() {
            return "Usage{" +
                    "promptTokens=" + promptTokens +
                    ", completionTokens=" + completionTokens +
                    ", totalTokens=" + totalTokens +
                    ", completionTokensDetails=" + completionTokensDetails +
                    ", promptTokensDetails=" + promptTokensDetails +
                    '}';
        }

    }

    public static class CompletionTokensDetails {
        @SerializedName("accepted_prediction_tokens")
        private int acceptedPredictionTokens;

        @SerializedName("audio_tokens")
        private int audioTokens;

        @SerializedName("reasoning_tokens")
        private int reasoningTokens;

        @SerializedName("rejected_prediction_tokens")
        private int rejectedPredictionTokens;

        // Getters and Setters
        public int getAcceptedPredictionTokens() {
            return acceptedPredictionTokens;
        }

        public void setAcceptedPredictionTokens(int acceptedPredictionTokens) {
            this.acceptedPredictionTokens = acceptedPredictionTokens;
        }

        public int getAudioTokens() {
            return audioTokens;
        }

        public void setAudioTokens(int audioTokens) {
            this.audioTokens = audioTokens;
        }

        public int getReasoningTokens() {
            return reasoningTokens;
        }

        public void setReasoningTokens(int reasoningTokens) {
            this.reasoningTokens = reasoningTokens;
        }

        public int getRejectedPredictionTokens() {
            return rejectedPredictionTokens;
        }

        public void setRejectedPredictionTokens(int rejectedPredictionTokens) {
            this.rejectedPredictionTokens = rejectedPredictionTokens;
        }

        @Override
        public String toString() {
            return "CompletionTokensDetails{" +
                    "acceptedPredictionTokens=" + acceptedPredictionTokens +
                    ", audioTokens=" + audioTokens +
                    ", reasoningTokens=" + reasoningTokens +
                    ", rejectedPredictionTokens=" + rejectedPredictionTokens +
                    '}';
        }

    }

    public static class PromptTokensDetails {
        @SerializedName("audio_tokens")
        private int audioTokens;

        @SerializedName("cached_tokens")
        private int cachedTokens;

        // Getters and Setters
        public int getAudioTokens() {
            return audioTokens;
        }

        public void setAudioTokens(int audioTokens) {
            this.audioTokens = audioTokens;
        }

        public int getCachedTokens() {
            return cachedTokens;
        }

        public void setCachedTokens(int cachedTokens) {
            this.cachedTokens = cachedTokens;
        }

        @Override
        public String toString() {
            return "PromptTokensDetails{" +
                    "audioTokens=" + audioTokens +
                    ", cachedTokens=" + cachedTokens +
                    '}';
        }

    }

}