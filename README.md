# ✨ AltEgo —— Create Another You ✨

![AltEgo](img/altego-title.jpg)
<div align="center">
  
[![GitHub stars](https://img.shields.io/github/stars/altegox/altego?style=social)](https://github.com/altegox/altego/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/altegox/altego?style=social)](https://github.com/altegox/altego/network/members)

[![License](https://img.shields.io/github/license/altegox/altego)](LICENSE)
![COMMIT](https://img.shields.io/github/last-commit/altegox/altego?style=flat-square)
![LANG](https://img.shields.io/badge/language-Java-7F52FF?style=flat-square)

</div>

---

## 🔍 项目简介

**AltEgo** 的名称来源于 “Alter Ego”，即 “另一个我”。

AltEgo 是一个基于 **Java** 开发的 **AI Agent 框架**，能够帮助开发者快速接入 **ChatGPT、DeepSeek** 等大模型。其灵活的架构支持模型的组合调用，并能轻松创建和管理自定义 AI 工作流。

## ✨ 主要特性

- **快速接入大模型**：支持 ChatGPT、DeepSeek 等主流 LLM。
- **可扩展 AI Agent**：采用插件式架构，便于集成自定义功能。
- **支持模型组合调用**：可以组合不同模型，如 DeepSeek Reasoner 和 OpenAI GPT。
- **流式数据处理**：支持异步与流式调用，提高响应速度。
- **易于部署与集成**：兼容 Spring Boot 和其他 Java 生态工具。

---

## 🚀 安装与使用

### 1️⃣ 准备

- **JDK 21 或更高版本**
- **Maven 或 Gradle 构建工具**
- **可用的大模型 API Key（如 OpenAI、DeepSeek）**

### 2️⃣ 添加依赖

#### 使用 Maven
```xml
<!-- 暂未提供 -->
<dependency>
  <groupId>org.altegox</groupId>
  <artifactId>altego-framework</artifactId>
  <version>0.0.1-bata</version>
</dependency>

<dependency>
  <groupId>org.altegox</groupId>
  <artifactId>altego-openai</artifactId>
  <version>0.0.1-bata</version>
</dependency>
```

#### 使用 Gradle
```gradle
// 暂未提供
dependencies {
    implementation 'org.altegox:altego-framework:0.0.1-bata'
    implementation 'org.altegox:altego-openai:0.0.1-bata'
}
```

### 3️⃣ 快速开始

#### **示例：连接 ChatGPT 并发送消息**
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
        String response = client.generate("你好");
        System.out.println(response);
    }
}
```

#### **示例：非流式调用**
```java
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
```

#### **示例：流式调用**
```java
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
```

#### **示例：组合调用（DeepClaude 形式）**
```java
@Test
void combinationChat() {
    LangModel model = OpenaiModel.builder()
            .stream(true)
            .reasoner(reasoner -> {
                reasoner.setBaseUrl(System.getenv("DEEPSEEK_BASE_URL"));
                reasoner.setApiKey(System.getenv("DEEPSEEK_API_KEY"));
                reasoner.setModelName("deepseek-reasoner");
                return reasoner;
            })
            .generate(generate -> {
                generate.setBaseUrl(System.getenv("OPENAI_BASE_URL"));
                generate.setApiKey(System.getenv("OPENAI_API_KEY"));
                generate.setModelName("gpt-4o");
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
    });
}
```

#### **示例：工具调用**
```java
public class Tools {
    
    @Tool(description = "获取当前操作系统名称")
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    @Tool(
            params = {
                    @Param(param = "city", required = true)
            },
            description = "获取今日天气"
    )
    public static String getWeather(String city) {
        return "多云";
    }

    @Tool(
            params = {
                    @Param(param = "a", required = true),
                    @Param(param = "b", required = true)
            },
            description = "计算两个数的和"
    )
    public int add(int a, int b) {
        return a + b;
    }

    @Tool(
            params = {
                    @Param(param = "numbers", required = true)
            },
            description = "计算多个数的和"
    )
    // 启用tool缓存，此tool允许在参数相同的情况下直接走缓存返回运行结果而并非真正运行
    @ToolCache
    public int add(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

}
```

---

## 🛠️ 配置

建议在 `.env` 文件或系统环境变量中配置, 避免信息的泄漏：

```env
OPENAI_BASE_URL=https://api.openai.com/v1
OPENAI_API_KEY=your-api-key
DEEPSEEK_BASE_URL=https://api.deepseek.com
DEEPSEEK_API_KEY=your-api-key
```

---

## 🤝 贡献指南

欢迎贡献代码、优化文档或提交 Issue！

### 贡献步骤
1. **Fork 本仓库**
2. **创建新分支** (`git checkout -b feature-xxx`)
3. **提交修改** (`git commit -m 'Add new feature'`)
4. **推送分支** (`git push origin feature-xxx`)
5. **创建 Pull Request**
