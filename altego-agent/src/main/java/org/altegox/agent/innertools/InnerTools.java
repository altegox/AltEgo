package org.altegox.agent.innertools;

import org.altegox.agent.BaseAgent;
import org.altegox.agent.context.AgentConfig;
import org.altegox.agent.context.AgentContext;
import org.altegox.agent.utils.FileUtils;
import org.altegox.framework.annotation.Agent;
import org.altegox.framework.annotation.Param;
import org.altegox.framework.annotation.Tool;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.toolcall.executer.CodeExecuter;
import org.altegox.framework.toolcall.executer.ShellExecuter;

@Agent(
        name = "InterTools",
        description = "AltEgo framework inner tools.",
        group = "inner-tools"
)
public class InnerTools {

    @Tool(
            description = "保存文件到指定路径",
            params = {
                    @Param(param = "path", description = "文件保存的路径", required = false),
                    @Param(param = "fileName", description = "文件名，文件名要有意义，拓展名必须为'.txt'格式，例如: task.txt"),
                    @Param(param = "content", description = "需要保存在文件中的内容")
            }
    )
    private boolean saveFile(String path, String fileName, String content) {
        return FileUtils.saveFile(AltegoConfig.getFileSavePath(), fileName, content);
    }

    @Tool(
            description = "创建一个Agent，帮助你完成一些子任务",
            params = {
                    @Param(param = "prompt", description = "Agent的提示语")
            }
    )
    private String createAgent(String prompt) {
        AgentContext context = AgentContext.getContext();
        AgentConfig agentConfig = context.getAgentConfigByGroup("inner-tools");
        BaseAgent agent = BaseAgent.builder()
                .name(agentConfig.agentName())
                .prompt(prompt)
                .baseUrl(agentConfig.baseUrl())
                .apiKey(agentConfig.apiKey())
                .modelName(agentConfig.modelName())
                .build();
        return agent.execute();
    }

    @Tool(
            description = "执行Java代码，返回执行结果",
            params = {
                    @Param(param = "code", description = "需要执行的代码, 请包含main方法与导包信息")
            }
    )
    private String codeExecute(String code) {
        CodeExecuter codeExecuter = new CodeExecuter();
        return codeExecuter.exec(code);
    }

    @Tool(
            description = "提供cmd命令执行能力，返回执行结果",
            params = {
                    @Param(param = "cmd", description = "需要运行的命令，例如等'ls -l'，不同的系统命令可能不同")
            }
    )
    private String cmdExecute(String cmd) {
        ShellExecuter shellExecuter = new ShellExecuter();
        return shellExecuter.exec(cmd);
    }

    @Tool(
            description = "获取当前操作系统的名称"
    )
    private String getOsName() {
        return System.getProperty("os.name");
    }


}
