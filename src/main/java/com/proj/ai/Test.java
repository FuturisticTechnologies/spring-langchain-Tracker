package com.proj.ai;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class Test {
    public static void main(String[] args) {
        OperatingSystemMXBean os =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        System.out.println("System CPU: " + os.getCpuLoad());
        System.out.println("Process CPU: " + os.getProcessCpuLoad());
    }
}