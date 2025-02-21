package api;

import org.junit.jupiter.api.Test;
import org.rangenx.api.OpenaiClient;
import org.rangenx.model.OpenaiModel;

public class TestChatService {

    @Test
    void testchat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl("https://yunwu.ai/v1/chat/completions")
                .apiKey("sk-nGixhq1saUIgfX7YskwusqFDNUbHqOOtWccHLaCm5MKpxK4u")
                .modelName("gpt-4o-mini")
                .stream(false)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        String response = client.chat("你好");
        System.out.println(response);
    }

}
