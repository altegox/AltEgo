package org.altegox.framework.service;

import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.ModelResponse;

import java.util.List;

public interface ChatService<T> {

    String generate(String message);

    ModelResponse<T> chat(List<Message> messages);

    ModelResponse<T> chat(DefaultRequest request);

//    String completions(String message);
//
//    ModelResponse<T> completions(List<Message> messages);
//
//    <R extends DefaultRequest> ModelResponse<T> completions(R request);

}
