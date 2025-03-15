package org.altegox.framework.toolcall.executer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ShellExecuter implements Executer {

    private static final Logger log = LogManager.getLogger("[ShellExecuter]");

    @Override
    public String exec(String cmd) {
        StringBuilder output = new StringBuilder();
        try {
            String command = isFile(cmd) ? readFile(cmd) : cmd;
            Process process = Runtime.getRuntime().exec(getCommandPrefix() + command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }
            process.waitFor();
        } catch (Exception e) {
            log.error("Failed to execute command:{}, {}.", cmd, e.getMessage());
        }
        return output.toString().trim();
    }

    @Override
    public boolean exec(String cmd, String outPath) {
        try (FileWriter writer = new FileWriter(outPath)) {
            String command = isFile(cmd) ? readFile(cmd) : cmd;
            Process process = Runtime.getRuntime().exec(getCommandPrefix() + command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }
            process.waitFor();
            return true;
        } catch (Exception e) {
            log.error("[output] Failed to execute command:{}, {}.", cmd, e.getMessage());
            return false;
        }
    }

    private boolean isFile(String cmdOrPath) {
        File file = new File(cmdOrPath);
        return file.exists() && file.isFile();
    }

    private String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString().trim();
    }

    private String getCommandPrefix() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd /c ";
        } else {
            return "bash -c ";
        }
    }
}
