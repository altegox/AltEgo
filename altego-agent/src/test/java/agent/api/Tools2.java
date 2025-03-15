package agent.api;

import org.altegox.agent.BaseAgent;
import org.altegox.framework.annotation.Agent;
import org.altegox.framework.annotation.Param;
import org.altegox.framework.annotation.Tool;

import java.util.List;

@Agent(
        name = "获取用户当地天气",
        group = "weather"
)
public class Tools2 {

//    @Tool(description = "获取当前操作系统名称")
//    public static String getOSName() {
//        return System.getProperty("os.name");
//    }

    @Tool(
            params = {
                    @Param(param = "city", description = "城市名称", required = true)
            },
            description = "获取今日天气"
    )
    public static String getWeather(String city) {
        return "多云";
    }

    @Tool(
            description = "获取用户城市"
    )
    public static String getCity() {
        return "泰安";
    }


//
//    @Tool(
//            params = {
//                    @Param(param = "a", description = "第一个数", required = true),
//                    @Param(param = "b", description = "第二个数", required = true)
//            },
//            description = "计算两个数的和"
//    )
//    public int add(int a, int b) {
//        return a + b;
//    }
//
//    @Tool(
//            params = {
//                    @Param(param = "numbers", description = "数字列表", required = true)
//            },
//            description = "计算多个数的和"
//    )
//    public int add(List<Integer> numbers) {
//        return numbers.stream().mapToInt(Integer::intValue).sum();
//    }

}
