package org.altegox.toolcall;

import org.altegox.framework.annotation.Exclude;
import org.altegox.framework.annotation.Tool;
import org.altegox.framework.annotation.ToolCache;

import java.util.List;

//@Tool
public class Tools {

    TestConstruct testConstruct;

    public Tools(TestConstruct testConstruct) {
        this.testConstruct = testConstruct;
    }

//    @Tool
//    public static String getOSName() {
//        return System.getProperty("os.name");
//    }
//    @Tool
//    public static String getTodayWeather(String city) {
//        return "多云";
//    }
//    @Tool
//    public void printTestConstruct() {
//        System.out.println(testConstruct.toString());
//    }
//    @Tool
//    public int add(int a, int b) {
//        return a + b;
//    }
//    @Tool
//    public int add(List<Integer> numbers) {
//        return numbers.stream().mapToInt(Integer::intValue).sum();
//    }
//    @Tool
//    public void onlyPrint() {
//        System.out.println("onlyPrint");
//    }
//
//    @Exclude
//    public void excludePrint() {
//        System.out.println("Exclude");
//    }
//    @Tool
//    @ToolCache
//    public String cachePrint() {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "useCache";
//    }
//    @Tool
//    @ToolCache
//    @Exclude(annotation = ToolCache.class)
//    public String excludeCachePrint() {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "noCache";
//    }

}
