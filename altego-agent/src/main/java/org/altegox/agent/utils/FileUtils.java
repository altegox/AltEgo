package org.altegox.agent.utils;

import org.altegox.common.log.Log;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    public static String getFileName(String filePath) {
        int lastSeparatorIndex = filePath.lastIndexOf('/');
        if (lastSeparatorIndex != -1) {
            return filePath.substring(lastSeparatorIndex + 1);
        }
        return filePath;
    }

    public static boolean saveFile(String filePath, String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(filePath + "/" + fileName);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            Log.error("Error saving file: " + e.getMessage());
            return false;
        }
    }

    public static String readFile(String fileSavePath, String fileName) {
        try {
            FileReader reader = new FileReader(fileSavePath + "/" + fileName);
            StringBuilder content = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                content.append((char) c);
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            Log.error("Error reading file: " + e.getMessage());
            return null;
        }
    }

}
