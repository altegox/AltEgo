package org.altegox.framework.api.request;

import com.google.gson.annotations.SerializedName;
import org.altegox.framework.api.response.ChatResponse;

import java.util.List;

public class Message {

    private String content;
    private Object refusal;
    private String role;
    @SerializedName("tool_calls")
    private List<ChatResponse.ToolCall> toolCalls;
    @SerializedName("function_call")
    private ChatResponse.FunctionCall functionCall;
    private String audio;


    @SerializedName("tool_call_id")
    private String toolCallID;

    public Message() {
    }

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message(String role, String content, String toolCallID) {
        this.role = role;
        this.content = content;
        this.toolCallID = toolCallID;
    }



    public static Message of(String role, String content) {
        return new Message(role, content);
    }

    public static Message system(String content) {
        return new Message("system", content);
    }

    public static Message assistant(String content) {
        return new Message("assistant", content);
    }

    public static Message user(String content) {
        return new Message("user", content);
    }

    public static Message tool(String content, String toolCallID) {
        return new Message("tool", content, toolCallID);
    }

    //region Message getter and setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getRefusal() {
        return refusal;
    }

    public void setRefusal(Object refusal) {
        this.refusal = refusal;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ChatResponse.ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ChatResponse.ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

    public ChatResponse.FunctionCall getFunctionCall() {
        return functionCall;
    }

    public void setFunctionCall(ChatResponse.FunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    //endregion

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", refusal=" + refusal +
                ", role='" + role + '\'' +
                ", toolCalls=" + toolCalls +
                ", functionCall=" + functionCall +
                ", audio='" + audio + '\'' +
                ", toolCallID='" + toolCallID + '\'' +
                '}';
    }

}
