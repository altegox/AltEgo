package api;

import org.junit.jupiter.api.Test;
import org.altego.api.OpenaiClient;
import org.altego.model.OpenaiModel;

public class TestChatService {

    @Test
    void testchat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl("")
                .apiKey("")
                .modelName("gpt-4o-mini")
                .stream(false)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        String response = client.chat("你好");
        System.out.println(response);
    }

}
