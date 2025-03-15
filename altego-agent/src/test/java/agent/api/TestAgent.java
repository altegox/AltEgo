package agent.api;

import org.altegox.agent.BaseAgent;
import org.altegox.framework.AltegoFramework;
import org.altegox.framework.config.AltegoConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestAgent {

    @BeforeAll
    static void initialize() {
        AltegoConfig.enableToolCache();
        AltegoFramework.init();
    }

    @Test
    void testAgent() {
        BaseAgent agent = BaseAgent.builder()
                .name("weather")
                .prompt("今天天气怎么样")
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o")
                .build();
        String execute = agent.execute();
        System.out.println(execute);
    }

}
