
# AltEgo

# ![AltEgo](img/altego-title.jpg)

[![License](https://img.shields.io/github/license/altegox/altego)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/altegox/altego?style=social)](https://github.com/altegox/altego/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/altegox/altego?style=social)](https://github.com/altegox/altego/network/members)

AltEgo 是一个用 **Java** 编写的 **AI Agent 框架**，开发者可以使用它快速接入 **ChatGPT、DeepSeek** 等大模型，并轻松创建自己的 AI 工作流。

## ✨ 特性
- **快速接入大模型**：支持 ChatGPT、DeepSeek 等主流 LLM。
- **可扩展 AI Agent**：灵活的插件式架构，轻松集成自定义功能。
- **流式处理**：支持异步和流式数据处理，响应速度快。

## 🚀 安装与使用

### 1️⃣ 添加依赖
在 **Maven** 或 **Gradle** 项目中引入：

#### Maven
```xml
  暂未提供
```

#### Gradle
```gradle
dependencies {
  暂未提供
}
```

### 2️⃣ 快速开始
使用 Altego 连接 ChatGPT 并发送消息：

```java
public class TestChatService {

    @Test
    void easyChat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .stream(false)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        String response = client.chat("你好");
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

    /* 流式调用 */
    @Test
    void streamChat() {
        OpenaiModel model = OpenaiModel.builder()
                .baseUrl(System.getenv("OPENAI_BASE_URL"))
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .stream(true)
                .build();

        OpenaiClient client = OpenaiClient.create(model);
        ModelResponse<ChatResponse> response = client.chat(List.of(
                Message.user("你好"),
                Message.assistant("我是你的智能助手。"),
                Message.user("你是哪个模型？")
        ));
        response.stream().subscribe(chatResponse -> {
            System.out.println(chatResponse.toString());
        });
    }

}
```

## 🤝 贡献
欢迎贡献代码、优化文档或提交 Issue：
1. **Fork 本仓库**
2. **创建新分支** (`git checkout -b feature-xxx`)
3. **提交修改** (`git commit -m 'Add new feature'`)
4. **推送分支** (`git push origin feature-xxx`)
5. **创建 Pull Request**
