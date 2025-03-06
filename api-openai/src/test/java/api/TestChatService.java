package api;

import org.altegox.framework.api.LangModel;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.ChatResponse;
import org.altegox.framework.api.response.ModelResponse;
import org.altegox.framework.client.CombinationClient;
import org.junit.jupiter.api.Test;
import org.altegox.api.OpenaiClient;
import org.altegox.api.OpenaiModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestChatService {

    @Test
    void easyChat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("deepseek-reasoner")
                .stream(false)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        String response = client.generate("你好");
        System.out.println(response);
    }

    /* 非流式调用 */
    @Test
    void syncChat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
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
                .reasoner(reasoner -> {
                    reasoner.setBaseUrl(System.getenv("DEEPSEEK_BASE_URL"));
                    reasoner.setApiKey(System.getenv("DEEPSEEK_API_KEY"));
                    reasoner.setModelName("deepseek-reasoner");
                    reasoner.setStream(true);
                    return reasoner;
                })
                .generate(generate -> {
                    generate.setBaseUrl(System.getenv("OPENAI_BASE_URL"));
                    generate.setApiKey(System.getenv("OPENAI_API_KEY"));
                    generate.setModelName("gpt-4o");
                    generate.setStream(true);
                    return generate;
                })
                .build();

        CombinationClient client = CombinationClient.create(model);
        ModelResponse<ChatResponse> response = client.chat(List.of(Message.user("0.9和0.11哪个更大？")));
        response.stream().subscribe(chatResponse -> {
            if (chatResponse.getChoices().getFirst().getDelta().getReasoningContent() != null)
                System.out.print(chatResponse.getChoices().getFirst().getDelta().getReasoningContent());
            if (chatResponse.getChoices().getFirst().getDelta().getContent() != null)
                System.out.print(chatResponse.getChoices().getFirst().getDelta().getContent());
        }, e -> latch.countDown(), latch::countDown);
        latch.await();
    }

}
