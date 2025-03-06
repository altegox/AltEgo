package org.altegox.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.altegox.common.utils.Json;
import org.altegox.framework.AltegoFramework;
import org.altegox.framework.api.HttpClient;
import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.DefaultChatResponse;
import org.altegox.framework.config.AltegoConfig;

import org.altegox.framework.toolcall.CommandParser;
import org.altegox.framework.toolcall.caller.Caller;
import org.altegox.framework.toolcall.caller.ToolCaller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class TestHttpClient {

    @BeforeAll
    static void initialize() {
        AltegoConfig.enableToolCache();
        AltegoFramework.init("org.altegox");
    }

    @Test
    void testFluxPost() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        HttpClient client = new HttpClient(System.getenv("OPEN_AI_URL"),
                System.getenv("OPEN_AI_API_KEY"));
        DefaultRequest request = new DefaultRequest();
        request.setModel("gpt-4o-mini");
        String content = """
                你可以调用我本地的函数，按以下格式调用：
                INVOKE <函数名> <参数1> <参数2>: <返回值类型>
                例1：INVOKE add(Int 22, Int 44): Int
                例2：INVOKE test(String "this is string"): Int
                -----------------------------------------
                以下是支持的函数：
                1. getTodayWeather(String: city): String
                -----------------------------------------
                以下是用户的问题：
                今天泰安的天气怎么样
                """;
        request.setMessages(List.of(new Message("user", content)));
        request.setStream(true);

        Flux<String> post = client.post(request);
        StringBuilder result = new StringBuilder();
        Caller<String> caller = new ToolCaller<>();
        CommandParser parser = new CommandParser();
        parser.parse("INVOKE add(Int 22, Int 44): Int");
        post.subscribe(data -> {
            if (!"[DONE]".equals(data)) {
                GPT4oResponse response = Json.fromJson(data, GPT4oResponse.class);
                result.append(response.getChoices().getFirst().getDelta().getContent() == null ?
                        "" : response.getChoices().getFirst().getDelta().getContent());
            }
        }, error -> {
            System.err.println("Error: " + error.getMessage());
            latch.countDown();
        }, latch::countDown);

        latch.await();
        String[] parse = parser.parse(result.toString());
        Object[] objects = Arrays.copyOfRange(parse, 1, parse.length);
        String called = caller.call(parse[0], objects);
        System.out.println(called);

    }

    @Test
    void testMonoPost() throws InterruptedException {
        HttpClient client = new HttpClient(System.getenv("OPEN_AI_URL"),
                System.getenv("OPEN_AI_API_KEY"));
        DefaultRequest request = new DefaultRequest();
        request.setModel("gpt-4o-mini");
        request.setMessages(List.of(new Message("user", "你好")));
        request.setStream(false);

        Mono<String> response = client.postSync(request);
        String jsonData = response.block();
        DefaultChatResponse chatResponse = Json.fromJson(jsonData, DefaultChatResponse.class);
        System.out.println(chatResponse.toString());
    }

}
