package com.proj.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proj.ai.SystemTools;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SystemToolsTest {

    private SystemTools systemTools;

    @BeforeEach
    void setUp() {
        systemTools = new SystemTools();
    }

    @Test
    void testPingReturnsPong() {
        assertEquals("pong", systemTools.ping());
    }

    @Test
    void testHostMetadataIsNotEmpty() {
        assertNotNull(systemTools.getHostname());
        assertFalse(systemTools.getHostname().isEmpty());
        
        assertNotNull(systemTools.getIpAddress());
        assertFalse(systemTools.getIpAddress().isEmpty());
    }

    @Test
    void testOperatingSystemProperties() {
        assertNotNull(systemTools.getOperatingSystem());
        assertNotNull(systemTools.getOperatingSystemVersion());
        assertNotNull(systemTools.getOperatingSystemArchitecture());
        assertNotNull(systemTools.getCurrentUser());
    }

    @Test
    void testCpuUsageMetrics() {
        double cpu = systemTools.getCpuUsage();
        double processCpu = systemTools.getProcessCpuUsage();
        int processors = systemTools.getAvailableProcessors();

        // CPU metrics can sometimes return -100 if the system bean is warming up, 
        // but they should always fall within a realistic percentage bound or 0.
        assertTrue(cpu >= 0 && cpu <= 100, "CPU usage should be between 0% and 100%");
        assertTrue(processCpu >= 0 && processCpu <= 100, "Process CPU usage should be between 0% and 100%");
        assertTrue(processors > 0, "Available processors must be greater than zero");
    }

    @Test
    void testMemoryMetrics() {
        assertTrue(systemTools.getTotalPhysicalMemoryGB() > 0, "Total memory must be positive");
        assertTrue(systemTools.getFreePhysicalMemoryGB() >= 0, "Free memory cannot be negative");
        
        double usagePct = systemTools.getMemoryUsagePercentage();
        assertTrue(usagePct >= 0 && usagePct <= 100, "Memory percentage must be between 0 and 100");
    }

    @Test
    void testJvmMemoryUsageMap() {
        Map<String, Object> jvmMemory = systemTools.getJvmMemoryUsage();
        
        assertNotNull(jvmMemory);
        assertTrue(jvmMemory.containsKey("initMB"));
        assertTrue(jvmMemory.containsKey("usedMB"));
        assertTrue(jvmMemory.containsKey("committedMB"));
        assertTrue(jvmMemory.containsKey("maxMB"));
    }

    @Test
    void testDiskSpaceMetrics() {
        // Evaluate using the current execution working directory root
        String userDir = System.getProperty("user.dir");
        
        long totalSpace = systemTools.getTotalDiskSpace(userDir);
        long freeSpace = systemTools.getFreeDiskSpace(userDir);
        double usagePct = systemTools.getDiskUsagePercentage(userDir);

        assertTrue(totalSpace > 0, "Total disk space should be greater than 0");
        assertTrue(freeSpace >= 0, "Free disk space cannot be negative");
        assertTrue(usagePct >= 0 && usagePct <= 100, "Disk usage percentage must be within 0-100");
    }

    @Test
    void testInvalidDiskPathReturnsNegativeOrError() {
        String invalidPath = "/non-existent-folder-xyz-123";
        
        assertEquals(-1, systemTools.getTotalDiskSpace(invalidPath));
        assertEquals(-1, systemTools.getFreeDiskSpace(invalidPath));
        assertEquals(-1, systemTools.getDiskUsagePercentage(invalidPath));
        
        Map<String, Object> errorInfo = systemTools.getDiskInformation(invalidPath);
        assertTrue(errorInfo.containsKey("error"));
    }
}
