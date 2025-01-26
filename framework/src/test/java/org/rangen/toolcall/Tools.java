package org.rangen.toolcall;

import org.rangenx.annotation.Exclude;
import org.rangenx.annotation.Tool;
import org.rangenx.annotation.ToolCache;

import java.util.List;

@Tool
public class Tools {

    public static String getOSName() {
        return System.getProperty("os.name");
    }

//    public int add(int a, int b) {
//        return a + b;
//    }

    public int add(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    public void onlyPrint() {
        System.out.println("onlyPrint");
    }

    @Exclude
    public void excludePrint() {
        System.out.println("Exclude");
    }

    @ToolCache
    public String cachePrint() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "useCache";
    }

    @ToolCache
    @Exclude(annotation = ToolCache.class)
    public String excludeCachePrint() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "noCache";
    }

}
