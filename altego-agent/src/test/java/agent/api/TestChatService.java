package agent.api;

import org.altegox.framework.AltegoFramework;
import org.altegox.framework.model.LangModel;
import org.altegox.framework.entity.request.Message;
import org.altegox.framework.entity.response.ChatResponse;
import org.altegox.framework.entity.response.ModelResponse;
import org.altegox.framework.client.CombinationClient;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.toolcall.ToolManager;
import org.altegox.framework.toolcall.caller.Caller;
import org.altegox.framework.toolcall.caller.ToolCaller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.altegox.api.OpenaiClient;
import org.altegox.api.OpenaiModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChatService {

    @BeforeAll
    static void initialize() {
        AltegoConfig.enableToolCache();
        AltegoFramework.init();
    }

    @Test
    void testToolCaller() {
        System.out.println("-------------- ToolCaller --------------");
        Caller<Integer> caller = new ToolCaller<>();
        Integer result = caller.call("add", 1, 2);
        assertEquals(3, result, "1 + 2 should equal 3");
    }

    @Test
    void easyChat() throws InterruptedException {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o")
                .tools(ToolManager.getInstance().getToolsByGroup("weather"))
                .stream(true)
                .build();
        CountDownLatch latch = new CountDownLatch(1);
        OpenaiClient client = OpenaiClient.create(model);
//        String response = client.generate("你好,泰安天气怎么样");
        ModelResponse<ChatResponse> response = client.chat(List.of(Message.user("你好,今天天气怎么样")));
        System.out.println(response.response());
//        System.out.println(modelResponse.response());
//        response.stream().subscribe(chatResponse -> {
//            System.out.println(chatResponse.toString());
////            if (chatResponse.getChoices().getFirst().getDelta().getReasoningContent() != null)
////                System.out.print(chatResponse.getChoices().getFirst().getDelta().getReasoningContent());
////            if (chatResponse.getChoices().getFirst().getDelta().getContent() != null)
////                System.out.print(chatResponse.getChoices().getFirst().getDelta().getContent());
//        }, e -> latch.countDown(), latch::countDown);
//        latch.await();
    }

    /* 非流式调用 */
    @Test
    void syncChat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o")
                .modelName("gpt-4o-mini")
                .stream(false)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        ModelResponse<ChatResponse> response = client.chat(List.of(
                Message.user("你好"),
                Message.assistant("我是你的智能助手。"),
                Message.user("你是哪个模型？")
        ));
        System.out.println(response.response());
    }

    /* 非流式调用 */
    @Test
    void streamChat() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("deepseek-reasoner")
                .stream(true)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        ModelResponse<ChatResponse> response = client.chat(List.of(
                Message.user("0.9和0.11哪个更大？")
        ));
        StringBuilder str = new StringBuilder();
        response.stream().subscribe(chatResponse -> {
                    System.out.println(chatResponse.toString());
                }, e -> latch.countDown(),
                latch::countDown);
        latch.await();
        System.out.println(str);

    }


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        LangModel model = OpenaiModel.builder()
                .stream(true)
                .reasoner(reasoner -> reasoner
                        .baseUrl(System.getenv("OPENAI_BASE_URL"))
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .modelName("deepseek-reasoner")
                )
                .generate(generate -> generate
                        .baseUrl(System.getenv("OPENAI_BASE_URL"))
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .modelName("gpt-4o")
                )
                .build();

        CombinationClient client = CombinationClient.create(model);
        ModelResponse<ChatResponse> response = client.chat(List.of(Message.user("0.9和0.11哪个更大？")));
        response.stream().subscribe(chatResponse -> {
            if (chatResponse.getChoices().getFirst().getDelta().getReasoningContent() != null)
                System.out.print(chatResponse.getChoices().getFirst().getDelta().getReasoningContent());
            if (chatResponse.getChoices().getFirst().getDelta().getContent() != null){
                System.out.println("=====");
                System.out.print(chatResponse.getChoices().getFirst().getDelta().getContent());
            }
        }, e -> latch.countDown(), latch::countDown);
        latch.await();
    }

}
