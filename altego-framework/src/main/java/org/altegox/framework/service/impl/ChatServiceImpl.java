package org.altegox.framework.service.impl;

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

public class ChatServiceImpl<T extends LangModel> implements ChatService<ChatResponse> {
    protected final T model;
    protected final HttpClient httpClient;

    public ChatServiceImpl(T model) {
        this.model = model;
        this.httpClient = new HttpClient(model.getBaseUrl(), model.getApiKey());
    }

    @Override
    public String generate(String message) {
        DefaultRequest request = buildDefaultRequest(List.of(Message.user(message)));
        ChatResponse chatResponse = handleToolCalls(request, new ToolCaller<>());
        return chatResponse.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        DefaultRequest request = buildDefaultRequest(messages);
        return executeChat(request);
    }

    @Override
    public <R extends DefaultRequest> ModelResponse<ChatResponse> chat(R request) {
        if (request == null) throw new IllegalArgumentException("request is null");

        if (request.getModel() == null) {
            request.setModel(model.getModelName());
        }

        // todo: 处理参数合并
        if (request.getTools() == null && model.getTools() != null) {
            request.setTools(model.getTools());
        }

        return executeChat(request);
    }

    /**
     * 构建默认请求对象
     */
    private DefaultRequest buildDefaultRequest(List<Message> messages) {
        List<Message> messageList = new ArrayList<>(messages);
        if (!"system".equals(messageList.getFirst().getRole())) {
            messageList.addFirst(Message.system(model.getSystemMessage()));
        }
        messageList.addFirst(Message.system(model.getSystemMessage()));
        return DefaultRequest.builder()
                .model(model.getModelName())
                .stream(model.isStream())
                .messages(messageList)
                .tools(model.getTools())
                .build();
    }

    /**
     * 执行聊天请求并处理响应
     */
    private <R extends DefaultRequest> ModelResponse<ChatResponse> executeChat(R request) {
        if (model.getTools() != null) {
            ChatResponse chatResponse = handleToolCalls(request, new ToolCaller<>());
            return ModelResponse.of(chatResponse);
        }

        if (model.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            ChatListener<ChatResponse> listener = new ChatListener<>();
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }
    }

    /**
     * 处理工具调用逻辑
     *
     * @param request    请求对象
     * @param toolCaller 工具调用器
     * @return 最终的聊天响应
     */
    private <R extends DefaultRequest> ChatResponse handleToolCalls(R request, Caller<String> toolCaller) {
        // 工具调用暂不支持流式响应
        request.setStream(false);

        ChatListener<ChatResponse> listener = new ChatListener<>();
        httpClient.postSync(request, ChatResponse.class, listener);
        ChatResponse chatResponse = listener.onFinish();

        // 处理多轮工具调用
        while (isToolCallResponse(chatResponse)) {
            List<Message> messageList = new ArrayList<>(request.getMessages());
            messageList.add(chatResponse.getChoices().getFirst().getMessage());

            // 处理所有工具调用
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

        return chatResponse;
    }

    /**
     * 检查响应是否需要工具调用
     */
    private boolean isToolCallResponse(ChatResponse chatResponse) {
        return "tool_calls".equals(chatResponse.getChoices().getFirst().getFinishReason())
                && chatResponse.getChoices().getFirst().getMessage().getToolCalls() != null;
    }
}
