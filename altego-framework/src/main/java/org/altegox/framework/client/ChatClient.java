package org.altegox.framework.client;

import org.altegox.framework.service.impl.ChatServiceImpl;

import org.altegox.framework.model.LangModel;
import org.altegox.framework.entity.request.DefaultRequest;
import org.altegox.framework.entity.request.Message;
import org.altegox.framework.entity.response.ChatResponse;
import org.altegox.framework.entity.response.ModelResponse;
import org.altegox.framework.service.ChatService;

import java.util.List;

public abstract class ChatClient<T extends LangModel> {
    private final ChatService<ChatResponse> chatService;

    protected ChatClient(T model) {
        this.chatService = new ChatServiceImpl<>(model);
    }

    public String generate(String message) {
        return chatService.generate(message);
    }

    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        return chatService.chat(messages);
    }

    public ModelResponse<ChatResponse> chat(DefaultRequest request) {
        return chatService.chat(request);
    }
}