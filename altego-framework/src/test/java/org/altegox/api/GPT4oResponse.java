package org.altegox.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GPT4oResponse {

    private List<Choice> choices;
    private long created;
    private String id;
    private String model;
    private String object;
    
    @SerializedName("system_fingerprint")
    private String systemFingerprint;

    // Getter and Setter methods

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

    public String getSystemFingerprint() {
       return systemFingerprint;
    }

    public void setSystemFingerprint(String systemFingerprint) {
       this.systemFingerprint = systemFingerprint;
    }

    @Override
    public String toString() {
        return "ChatGpt4oResponse{" +
                "choices=" + choices +
                ", created=" + created +
                ", id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", object='" + object + '\'' +
                ", systemFingerprint='" + systemFingerprint + '\'' +
                '}';
    }

    public static class Choice {

        @SerializedName("content_filter_results")
        private ContentFilterResults contentFilterResults;
        private Delta delta;
        
        @SerializedName("finish_reason")
        private String finishReason;
        private int index;
        private Object logprobs;

        // Getter and Setter methods

        public ContentFilterResults getContentFilterResults() {
            return contentFilterResults;
        }

        public void setContentFilterResults(ContentFilterResults contentFilterResults) {
            this.contentFilterResults = contentFilterResults;
        }

        public Delta getDelta() {
            return delta;
        }

        public void setDelta(Delta delta) {
            this.delta = delta;
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

        @Override
        public String toString() {
            return "Choice{" +
                    "contentFilterResults=" + contentFilterResults +
                    ", delta=" + delta +
                    ", finishReason='" + finishReason + '\'' +
                    ", index=" + index +
                    ", logprobs=" + logprobs +
                    '}';
        }
    }

    public static class ContentFilterResults {
        private FilterResult hate;
        
        @SerializedName("self_harm")
        private FilterResult selfHarm;
        private FilterResult sexual;
        private FilterResult violence;

        // Getter and Setter methods

        public FilterResult getHate() {
            return hate;
        }

        public void setHate(FilterResult hate) {
            this.hate = hate;
        }

        public FilterResult getSelfHarm() {
            return selfHarm;
        }

        public void setSelfHarm(FilterResult selfHarm) {
            this.selfHarm = selfHarm;
        }

        public FilterResult getSexual() {
            return sexual;
        }

        public void setSexual(FilterResult sexual) {
            this.sexual = sexual;
        }

        public FilterResult getViolence() {
            return violence;
        }

        public void setViolence(FilterResult violence) {
            this.violence = violence;
        }

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

    public static class FilterResult {
        private boolean filtered;
        private String severity;

        // Getter and Setter methods

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

        @Override
        public String toString() {
            return "FilterResult{" +
                    "filtered=" + filtered +
                    ", severity='" + severity + '\'' +
                    '}';
        }
    }

}
