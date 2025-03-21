package org.altegox.toolcall;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.altegox.framework.AltegoFramework;
import org.altegox.common.log.Log;
import org.altegox.common.exception.ToolNotFindException;
import org.altegox.framework.config.AltegoConfig;
import org.altegox.framework.toolcall.caller.Caller;
import org.altegox.framework.toolcall.caller.ToolCaller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToolCallTest {

    @BeforeAll
    static void initialize() {
        AltegoConfig.enableToolCache();
        AltegoFramework.init();
    }

    @Test
    void testToolCaller() {
        System.out.println("-------------- ToolCaller --------------");
        Caller<Integer> caller = new ToolCaller<>();
        Integer result = caller.call("add", 1, 2);
        assertEquals(3, result, "1 + 2 should equal 3");
    }

    @Test
    void testAdd() {
        Caller<Integer> caller = new ToolCaller<>();
        Integer result = caller.call("add", 1, 2);
        assertEquals(3, result, "1 + 2 should equal 3");
    }

    @Test
    void testGetOSName() {
        Caller<String> caller = new ToolCaller<>();
        String osName = caller.call("getOSName");
        assertNotNull(osName, "OS name should not be null");
        Log.info("OS Name: " + osName);
    }

    @Test
    void testAddWithList() {
        Caller<Integer> caller = new ToolCaller<>();
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        Integer result = caller.call("add", numbers);
        assertEquals(10, result, "1 + 2 + 3 + 4 should equal 10");
    }

    @Test
    void testOnlyPrint() {
        Caller<Void> caller = new ToolCaller<>();
        caller.call("onlyPrint");
    }

    @Test
    void testExcludePrint() {
        Caller<Void> caller = new ToolCaller<>();
        assertThrows(ToolNotFindException.class, () -> caller.call("excludePrint"));
    }

    @Test
    void testCachePrint() {
        Caller<String> caller = new ToolCaller<>();
        caller.call("cachePrint");
        long start = System.currentTimeMillis();
        caller.call("cachePrint");
        long end = System.currentTimeMillis();
        assert (end - start) < 500;
    }

    @Test
    void testNoCachePrint() {
        Caller<String> caller = new ToolCaller<>();
        caller.call("excludeCachePrint");
        long start = System.currentTimeMillis();
        caller.call("excludeCachePrint");
        long end = System.currentTimeMillis();
        assert (end - start) >= 500;
    }

    @Test
    void testToolInstanceConstruct() {
        Caller<Void> caller = new ToolCaller<>();
        caller.call("printTestConstruct");
    }

}
