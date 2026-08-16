package com.proj.ai;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemDashboardController {

    private final SystemTools systemTools;

    public SystemDashboardController(SystemTools systemTools) {
        this.systemTools = systemTools;
    }

    
    @GetMapping("/test")
    public Map<String, String> test() {

        System.out.println("🔥 SYSTEM DASHBOARD CONTROLLER CALLED");

        return Map.of(
                "status", "success",
                "message", "SystemDashboardController is working"
        );
    }
    
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {

        Map<String, Object> result = new HashMap<>();

        // =====================================================
        // SERVER
        // =====================================================

        result.put("hostname",
                systemTools.getHostname());

        result.put("ipAddress",
                systemTools.getIpAddress());

        result.put("operatingSystem",
                systemTools.getOperatingSystem());

        result.put("osVersion",
                systemTools.getOperatingSystemVersion());

        result.put("architecture",
                systemTools.getOperatingSystemArchitecture());

        result.put("currentUser",
                systemTools.getCurrentUser());


        // =====================================================
        // CPU
        // =====================================================

        result.put("cpuUsage",
                systemTools.getCpuUsage());

        result.put("processCpuUsage",
                systemTools.getProcessCpuUsage());

        result.put("logicalProcessors",
                systemTools.getAvailableProcessors());


        // =====================================================
        // MEMORY
        // =====================================================

        result.put("totalMemoryGB",
                systemTools.getTotalPhysicalMemoryGB());

        result.put("freeMemoryGB",
                systemTools.getFreePhysicalMemoryGB());

        result.put("memoryUsagePercentage",
                systemTools.getMemoryUsagePercentage());


        // =====================================================
        // DISK - WINDOWS C DRIVE
        // =====================================================

        String drive = "C:\\";

        result.put("diskPath", drive);

        result.put("diskTotalGB",
                systemTools.getTotalDiskSpace(drive));

        result.put("diskFreeGB",
                systemTools.getFreeDiskSpace(drive));

        result.put("diskUsagePercentage",
                systemTools.getDiskUsagePercentage(drive));


        // =====================================================
        // JAVA / JVM
        // =====================================================

        result.put("javaVersion",
                systemTools.getJavaVersion());

        result.put("javaVendor",
                systemTools.getJavaVendor());

        result.put("jvmName",
                systemTools.getJvmName());

        result.put("jvmUptime",
                systemTools.getJvmUptime());

        result.put("liveThreads",
                systemTools.getLiveThreadCount());

        result.put("daemonThreads",
                systemTools.getDaemonThreadCount());


        // =====================================================
        // NETWORK
        // =====================================================

        result.put("networkAvailable",
                systemTools.isNetworkAvailable());

        result.put("macAddress",
                systemTools.getMacAddress());


        return result;
    }
}