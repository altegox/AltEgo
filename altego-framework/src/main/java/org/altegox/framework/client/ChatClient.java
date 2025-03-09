package org.altegox.framework.client;

import org.altegox.common.utils.Json;
import org.altegox.framework.service.listener.ChatListener;
import org.altegox.framework.api.HttpClient;
import org.altegox.framework.api.LangModel;
import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.ChatResponse;
import org.altegox.framework.api.response.ModelResponse;
import org.altegox.framework.service.ChatService;
import org.altegox.framework.toolcall.caller.Caller;
import org.altegox.framework.toolcall.caller.ToolCaller;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatClient<T extends LangModel> implements ChatService<ChatResponse> {

    protected final T model;
    protected final HttpClient httpClient;

    protected ChatClient(T model) {
        this.model = model;
        this.httpClient = new HttpClient(model.getBaseUrl(), model.getApiKey());
    }

    @Override
    public String generate(String message) {
        DefaultRequest request = DefaultRequest.builder()
                .model(model.getModelName())
                .messages(List.of(Message.user(message)))
                .tools(model.getTools())
                .build();

        ChatListener<ChatResponse> listener = new ChatListener<>();
        httpClient.postSync(request, ChatResponse.class, listener);
        ChatResponse chatResponse = listener.onFinish();
        while (isToolCallResponse(chatResponse)) {
            List<Message> messageList = new ArrayList<>();
            messageList.add(Message.user(message));
            messageList.add(chatResponse.getChoices().getFirst().getMessage());

            Caller<String> toolCaller = new ToolCaller<>();
            List<ChatResponse.ToolCall> toolCalls = chatResponse.getChoices().getFirst().getMessage().getToolCalls();
            toolCalls.forEach(toolCall -> {
                String toolName = toolCall.getFunction().getName();
                String toolArgs = Json.toJson(toolCall.getFunction().getArguments());
                String toolCallResult = toolCaller.call(toolName, toolArgs);
                messageList.add(Message.tool(toolCallResult, toolCall.getId()));
            });

            request.setMessages(messageList);
            httpClient.postSync(request, ChatResponse.class, listener);
            chatResponse = listener.onFinish();
        }
        return chatResponse.getChoices().getFirst().getMessage().getContent();

    }

    @Override
    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        DefaultRequest request = DefaultRequest.builder()
                .model(model.getModelName())
                .stream(model.isStream())
                .messages(messages)
                .tools(model.getTools())
                .build();

        ChatListener<ChatResponse> listener = new ChatListener<>();
        // 启用了tool_call
        if (model.getTools() != null) {
            request.setStream(false);
            Caller<String> toolCaller = new ToolCaller<>();
            httpClient.postSync(request, ChatResponse.class, listener);
            ChatResponse chatResponse = listener.onFinish();
            List<Message> messageList = new ArrayList<>(messages);
            messageList.add(chatResponse.getChoices().getFirst().getMessage());
            while (isToolCallResponse(chatResponse)) {
                List<ChatResponse.ToolCall> toolCalls = chatResponse.getChoices().getFirst().getMessage().getToolCalls();
                toolCalls.forEach(toolCall -> {
                    String toolName = toolCall.getFunction().getName();
                    String toolArgs = Json.toJson(toolCall.getFunction().getArguments());
                    String toolCallResult = toolCaller.call(toolName, toolArgs);
                    messageList.add(Message.tool(toolCallResult, toolCall.getId()));
                });
                request.setMessages(messageList);

                httpClient.postSync(request, ChatResponse.class, listener);
                chatResponse = listener.onFinish();
            }
            return ModelResponse.of(chatResponse);
        }
        if (model.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }

    }

    @Override
    public <R extends DefaultRequest> ModelResponse<ChatResponse> chat(R request) {
        if (request == null) throw new IllegalArgumentException("request is null");

        ChatListener<ChatResponse> listener = new ChatListener<>();
        // 启用了tool_call
        if (model.getTools() != null) {
            request.setStream(false);
            Caller<String> toolCaller = new ToolCaller<>();
            httpClient.postSync(request, ChatResponse.class, listener);
            ChatResponse chatResponse = listener.onFinish();
            List<Message> messageList = request.getMessages();
            messageList.add(chatResponse.getChoices().getFirst().getMessage());
            while (isToolCallResponse(chatResponse)) {
                List<ChatResponse.ToolCall> toolCalls = chatResponse.getChoices().getFirst().getMessage().getToolCalls();
                toolCalls.forEach(toolCall -> {
                    String toolName = toolCall.getFunction().getName();
                    String toolArgs = Json.toJson(toolCall.getFunction().getArguments());
                    String toolCallResult = toolCaller.call(toolName, toolArgs);
                    messageList.add(Message.tool(toolCallResult, toolCall.getId()));
                });

                request.setMessages(messageList);

                httpClient.postSync(request, ChatResponse.class, listener);
                chatResponse = listener.onFinish();
            }
            return ModelResponse.of(chatResponse);
        }

        if (model.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }
    }

    private boolean isToolCallResponse(ChatResponse chatResponse) {
        return "tool_calls".equals(chatResponse.getChoices().getFirst().getFinishReason())
                && chatResponse.getChoices().getFirst().getMessage().getToolCalls() != null;
    }

}