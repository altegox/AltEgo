package org.altegox.agent.innertools;

import org.altegox.agent.BaseAgent;
import org.altegox.agent.context.AgentConfig;
import org.altegox.agent.context.AgentContext;
import org.altegox.agent.utils.FileUtils;
import org.altegox.framework.annotation.Param;
import org.altegox.framework.annotation.Tool;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.toolcall.ToolEntity;
import org.altegox.framework.toolcall.executer.CodeExecuter;
import org.altegox.framework.toolcall.executer.ShellExecuter;

import java.util.List;

public class InnerTools {

    @Tool(
            description = "保存文件到指定路径",
            params = {
                    @Param(param = "fileName", description = "文件名，文件名要有意义，拓展名必须为'.txt'格式，例如: task.txt"),
                    @Param(param = "content", description = "需要保存在文件中的内容")
            },
            group = "inner-tools"
    )
    private String saveFile(String fileName, String content) {
        boolean savedFile = FileUtils.saveFile(AltegoConfig.getFileSavePath(), fileName, content);
        if (!savedFile) return "文件保存失败";
        return fileName;
    }

    @Tool(
            description = "读取文件内容",
            params = {
                    @Param(param = "fileName", description = "文件名，包含'.txt'的拓展名格式，例如: task.txt")
            },
            group = "inner-tools"
    )
    private String readFile(String fileName){
        return FileUtils.readFile(AltegoConfig.getFileSavePath(), fileName);
    }

    @Tool(
            description = "创建一个Agent，帮助你完成一些子任务",
            params = {
                    @Param(param = "prompt", description = "Agent的提示语"),
                    @Param(param = "tools", description = "从你的tool列表中选取部分可能使用到的Tool按指定格式传入")
            },
            group = "inner-tools"
    )
    private String createAgent(String prompt, List<ToolEntity> tools) {
        AgentContext context = AgentContext.getContext();
        AgentConfig agentConfig = context.getAgentConfig();
        BaseAgent agent = BaseAgent.builder()
                .tools(tools)
                .prompt(prompt)
                .baseUrl(agentConfig.getBaseUrl())
                .apiKey(agentConfig.getApiKey())
                .modelName(agentConfig.getModelName())
                .build();
        return agent.execute();
    }

    @Tool(
            description = "执行Java代码，返回执行结果",
            params = {
                    @Param(param = "code", description = "需要执行的代码, 请包含main方法与导包信息")
            },
            group = "inner-tools"
    )
    private String codeExecute(String code) {
        CodeExecuter codeExecuter = new CodeExecuter();
        return codeExecuter.exec(code);
    }

    @Tool(
            description = "提供cmd命令执行能力，返回执行结果",
            params = {
                    @Param(param = "cmd", description = "需要运行的命令，例如等'ls -l'，不同的系统命令可能不同")
            },
            group = "inner-tools"
    )
    private String cmdExecute(String cmd) {
        ShellExecuter shellExecuter = new ShellExecuter();
        return shellExecuter.exec(cmd);
    }

    @Tool(
            description = "获取当前操作系统的名称",
            group = "inner-tools"
    )
    private String getOsName() {
        return System.getProperty("os.name");
    }

}
