package org.altegox.toolcall;

import org.altegox.framework.annotation.Param;
import org.altegox.framework.annotation.Tool;

import java.util.List;

public class Tools2 {

    @Tool(description = "获取当前操作系统名称",
            group = "weather")
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    @Tool(
            params = {
                    @Param(param = "city", description = "城市名称", required = true)
            },
            description = "获取今日天气",
            group = "weather"
    )
    public static String getWeather(String city) {
        return "多云";
    }

    @Tool(
            params = {
                    @Param(param = "a", description = "第一个数", required = true),
                    @Param(param = "b", description = "第二个数", required = true)
            },
            description = "计算两个数的和"
    )
    public int add(int a, int b) {
        return a + b;
    }

    @Tool(
            params = {
                    @Param(param = "numbers", description = "数字列表", required = true)
            },
            description = "计算多个数的和"
    )
    public int add(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

}
