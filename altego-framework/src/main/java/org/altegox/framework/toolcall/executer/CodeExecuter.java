package org.altegox.framework.toolcall.executer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeExecuter implements Executer {

    private static final Logger log = LogManager.getLogger("[CodeExecuter]");

    @Override
    public String exec(String codeSource) {
        try {
            String code;
            String className;

            if (isFile(codeSource)) {
                code = readFile(codeSource);
                className = extractClassNameFromFile(codeSource);
            } else {
                code = codeSource;
                className = extractClassName(code);
            }

            Path tempDir = Files.createTempDirectory("tempJavaCode");
            File javaFile = new File(tempDir.toFile(), className + ".java");
            try (PrintWriter out = new PrintWriter(javaFile)) {
                out.write(code);
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return "Java compiler not available.";
            }
            int result = compiler.run(null, null, null, javaFile.getPath());
            if (result != 0) {
                return "Compilation failed.";
            }

            // 执行编译生成的 .class 文件
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), className);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 读取执行输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }
            process.waitFor();

            // 清理临时文件
            Files.deleteIfExists(javaFile.toPath());
            Files.deleteIfExists(tempDir.resolve(className + ".class"));
            Files.deleteIfExists(tempDir);

            return output.toString().trim();
        } catch (Exception e) {
            log.error("Failed to execute code: {}.", e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    private boolean isFile(String codeSource) {
        File file = new File(codeSource);
        return file.exists() && file.isFile();
    }

    private String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString().trim();
    }

    // 从代码中提取类名
    private String extractClassName(String code) {
        int classIndex = code.indexOf("class ");
        if (classIndex == -1) throw new IllegalArgumentException("Invalid Java code: No class found.");
        int startIndex = classIndex + 6;
        int endIndex = code.indexOf(" ", startIndex);
        return code.substring(startIndex, endIndex);
    }

    // 从文件路径中提取类名（假设类名与文件名相同）
    private String extractClassNameFromFile(String filePath) {
        String fileName = new File(filePath).getName();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    @Override
    public boolean exec(String codeSource, String outPath) {
        log.error("Code execution is not supported.");
        return false;
    }

}