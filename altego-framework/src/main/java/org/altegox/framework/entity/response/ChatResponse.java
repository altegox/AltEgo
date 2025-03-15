package org.altegox.framework.entity.response;

import com.google.gson.annotations.SerializedName;
import org.altegox.framework.entity.request.Message;

import java.util.List;

public class ChatResponse {
    private List<Choice> choices;
    private long created;
    private String id;
    private String model;
    private String object;

    @SerializedName("prompt_filter_results")
    private List<PromptFilterResult> promptFilterResults;

    @SerializedName("system_fingerprint")
    private String systemFingerprint;
    private Usage usage;

    //region DefaultChatResponse getter and setter

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<PromptFilterResult> getPromptFilterResults() {
        return promptFilterResults;
    }

    public void setPromptFilterResults(List<PromptFilterResult> promptFilterResults) {
        this.promptFilterResults = promptFilterResults;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public void setSystemFingerprint(String systemFingerprint) {
        this.systemFingerprint = systemFingerprint;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    //endregion

    @Override
    public String toString() {
        return "DefaultChatResponse{" +
                "choices=" + choices +
                ", created=" + created +
                ", id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", object='" + object + '\'' +
                ", promptFilterResults=" + promptFilterResults +
                ", systemFingerprint='" + systemFingerprint + '\'' +
                ", usage=" + usage +
                '}';
    }

    public static class Choice {
        @SerializedName("content_filter_results")
        private ContentFilterResults contentFilterResults;

        @SerializedName("finish_reason")
        private String finishReason;
        private Delta delta;
        private String text;
        private int index;
        private Object logprobs;
        private Message message;

        //region Choice getter and setter

        public ContentFilterResults getContentFilterResults() {
            return contentFilterResults;
        }

        public void setContentFilterResults(ContentFilterResults contentFilterResults) {
            this.contentFilterResults = contentFilterResults;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
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

        public Delta getDelta() {
            return delta;
        }

        public void setDelta(Delta delta) {
            this.delta = delta;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        //endregion

        @Override
        public String toString() {
            return "Choice{" +
                    "contentFilterResults=" + contentFilterResults +
                    ", finishReason='" + finishReason + '\'' +
                    ", index=" + index +
                    ", logprobs=" + logprobs +
                    ", message=" + message +
                    ", delta=" + delta +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    public static class Delta {
        private String role;
        private String content;
        private String refusal;
        @SerializedName("tool_calls")
        private List<ChatResponse.ToolCall> toolCalls;
        @SerializedName("reasoning_content")
        private String reasoningContent;

        public String getContent() {
            return content;
        }

        public String getRole() {
            return role;
        }

        public String getReasoningContent() {
            return reasoningContent;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setReasoningContent(String reasoningContent) {
            this.reasoningContent = reasoningContent;
        }

        public List<ChatResponse.ToolCall> getToolCalls() {
            return toolCalls;
        }

        public void setToolCalls(List<ChatResponse.ToolCall> toolCalls) {
            this.toolCalls = toolCalls;
        }

        public String getRefusal() {
            return refusal;
        }

        public void setRefusal(String refusal) {
            this.refusal = refusal;
        }

        @Override
        public String toString() {
            return "Delta{" +
                    "content='" + content + '\'' +
                    ", role='" + role + '\'' +
                    ", refusal='" + refusal + '\'' +
                    ", reasoningContent='" + reasoningContent + '\'' +
                    ", toolCalls=" + toolCalls +
                    '}';
        }
    }

    public static class ContentFilterResults {
        private FilterDetail hate;

        @SerializedName("self_harm")
        private FilterDetail selfHarm;
        private FilterDetail sexual;
        private FilterDetail violence;

        //region ContentFilterResults getter and setter

        public FilterDetail getHate() {
            return hate;
        }

        public void setHate(FilterDetail hate) {
            this.hate = hate;
        }

        public FilterDetail getSelfHarm() {
            return selfHarm;
        }

        public void setSelfHarm(FilterDetail selfHarm) {
            this.selfHarm = selfHarm;
        }

        public FilterDetail getSexual() {
            return sexual;
        }

        public void setSexual(FilterDetail sexual) {
            this.sexual = sexual;
        }

        public FilterDetail getViolence() {
            return violence;
        }

        public void setViolence(FilterDetail violence) {
            this.violence = violence;
        }

        //endregion

        @Override
        public String toString() {
            return "ContentFilterResults{" +
                    "hate=" + hate +
                    ", selfHarm=" + selfHarm +
                    ", sexual=" + sexual +
                    ", violence=" + violence +
                    '}';
        }
    }


    public static class FilterDetail {
        private boolean filtered;
        private String severity;

        //region FilterDetail getter and setter
        public boolean isFiltered() {
            return filtered;
        }

        public void setFiltered(boolean filtered) {
            this.filtered = filtered;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        //endregion

        @Override
        public String toString() {
            return "FilterDetail{" +
                    "filtered=" + filtered +
                    ", severity='" + severity + '\'' +
                    '}';
        }
    }

    public static class ToolCall {
        private String id;
        private FunctionCall function;
        private String type;
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
        private String name;
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

    public static class PromptFilterResult {
        @SerializedName("content_filter_results")
        private ContentFilterResults contentFilterResults;

        @SerializedName("prompt_index")
        private int promptIndex;

        //region PromptFilterResult getter and setter
        public ContentFilterResults getContentFilterResults() {
            return contentFilterResults;
        }

        public void setContentFilterResults(ContentFilterResults contentFilterResults) {
            this.contentFilterResults = contentFilterResults;
        }

        public int getPromptIndex() {
            return promptIndex;
        }

        public void setPromptIndex(int promptIndex) {
            this.promptIndex = promptIndex;
        }

        //endregion

        @Override
        public String toString() {
            return "PromptFilterResult{" +
                    "contentFilterResults=" + contentFilterResults +
                    ", promptIndex=" + promptIndex +
                    '}';
        }
    }

    public static class Usage {
        @SerializedName("completion_tokens")
        private int completionTokens;

        @SerializedName("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;

        @SerializedName("prompt_tokens")
        private int promptTokens;

        @SerializedName("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;

        @SerializedName("total_tokens")
        private int totalTokens;

        //region Usage getter and setter
        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public CompletionTokensDetails getCompletionTokensDetails() {
            return completionTokensDetails;
        }

        public void setCompletionTokensDetails(CompletionTokensDetails completionTokensDetails) {
            this.completionTokensDetails = completionTokensDetails;
        }

        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public PromptTokensDetails getPromptTokensDetails() {
            return promptTokensDetails;
        }

        public void setPromptTokensDetails(PromptTokensDetails promptTokensDetails) {
            this.promptTokensDetails = promptTokensDetails;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }

        //endregion

        @Override
        public String toString() {
            return "Usage{" +
                    "completionTokens=" + completionTokens +
                    ", completionTokensDetails=" + completionTokensDetails +
                    ", promptTokens=" + promptTokens +
                    ", promptTokensDetails=" + promptTokensDetails +
                    ", totalTokens=" + totalTokens +
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

        //region DefaultChatResponse getter and setter
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

        //endregion

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

        //region PromptTokensDetails getter and setter
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

        //endregion

        @Override
        public String toString() {
            return "PromptTokensDetails{" +
                    "audioTokens=" + audioTokens +
                    ", cachedTokens=" + cachedTokens +
                    '}';
        }
    }

}





