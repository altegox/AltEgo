package org.rangenx.framework.service;

import org.rangenx.framework.api.request.DefaultRequest;
import org.rangenx.framework.api.request.Message;

import java.util.List;

public interface ChatService<T> {

    String chat(String message);

    T chat(List<Message> messages);

    <R extends DefaultRequest> T chat(R request);

    String completions(String message);

    T completions(List<Message> messages);

    <R extends DefaultRequest> T completions(R request);

}
