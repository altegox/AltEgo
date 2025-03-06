package org.altegox.framework.toolcall;

import org.altegox.common.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    // 正则表达式解析语法，提取方法名、参数列表和返回类型
    private static final Pattern INVOKE_PATTERN = Pattern.compile(
            "INVOKE\\s+(\\w+)\\((.*?)\\)\\s*:\\s*(\\w+)"
    );

    public String[] parse(String input) {
        Matcher matcher = INVOKE_PATTERN.matcher(input);

        if (!matcher.matches()) {
            Log.error("Parsing failed, format error.");
        }

        // 解析方法名、参数列表和返回类型
        String methodName = matcher.group(1);
        String parameterList = matcher.group(2);

        // 解析参数
        List<Object> args = parseArguments(parameterList);

        // 构造返回值：方法名 + 参数值数组
        String[] result = new String[args.size() + 1];
        result[0] = methodName;
        for (int i = 0; i < args.size(); i++) {
            result[i + 1] = args.get(i).toString();
        }

        return result;
    }

    private static List<Object> parseArguments(String parameterList) {
        List<Object> args = new ArrayList<>();

        if (parameterList.trim().isEmpty()) {
            return args;
        }

        String[] parameters = parameterList.split(",");
        for (String param : parameters) {
            String[] parts = param.trim().split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("The parameter format is incorrect : " + param);
            }

            String type = parts[0];
            String value = parts[1];

            switch (type) {
                case "Int" -> args.add(Integer.parseInt(value));
                case "String" -> args.add(value);
                case "Double" -> args.add(Double.parseDouble(value));
                case "Float" -> args.add(Float.parseFloat(value));
                case "Long" -> args.add(Long.parseLong(value));
                case "Char" -> args.add(value.charAt(0));
                case "Boolean" -> args.add(Boolean.parseBoolean(value));
                default -> throw new IllegalArgumentException("不支持的参数类型: " + type);
            }

        }

        return args;
    }


}
