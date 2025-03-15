package org.altegox.framework.client;

import org.altegox.framework.service.ChatService;
import org.altegox.framework.service.impl.CombinationServiceImpl;
import org.altegox.framework.model.LangModel;
import org.altegox.framework.entity.request.DefaultRequest;
import org.altegox.framework.entity.request.Message;
import org.altegox.framework.entity.response.ChatResponse;
import org.altegox.framework.entity.response.ModelResponse;

import java.util.List;

public class CombinationClient {
    private final ChatService<ChatResponse> service;

    public static CombinationClient create(LangModel langModel) {
        return new CombinationClient(langModel);
    }

    protected CombinationClient(LangModel model) {
        this.service = new CombinationServiceImpl(model);
    }

    public String generate(String message) {
        return service.generate(message);
    }

    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        return service.chat(messages);
    }

    public ModelResponse<ChatResponse> chat(DefaultRequest request) {
        return service.chat(request);
    }
}
