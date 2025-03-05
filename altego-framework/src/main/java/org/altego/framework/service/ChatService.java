package org.altego.framework.service;

import org.altego.framework.api.request.DefaultRequest;
import org.altego.framework.api.request.Message;
import org.altego.framework.api.response.ModelResponse;

import java.util.List;

public interface ChatService<T> {

    String generate(String message);

    ModelResponse<T> chat(List<Message> messages);

    <R extends DefaultRequest> ModelResponse<T> chat(R request);

//    String completions(String message);
//
//    ModelResponse<T> completions(List<Message> messages);
//
//    <R extends DefaultRequest> ModelResponse<T> completions(R request);

}
