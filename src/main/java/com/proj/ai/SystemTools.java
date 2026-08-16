package com.proj.ai;

import com.sun.management.OperatingSystemMXBean;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SystemTools {

    private final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();


    // ============================================================
    // BASIC
    // ============================================================

    @Tool("Returns a test response to verify that the system tools are working")
    public String ping() {
        return "pong";
    }


    // ============================================================
    // SERVER INFORMATION
    // ============================================================

    @Tool("Returns the hostname of the current server")
    public String getHostname() {

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }


    @Tool("Returns the IP address of the current server")
    public String getIpAddress() {

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Unknown";
        }
    }


    @Tool("Returns the operating system name")
    public String getOperatingSystem() {

        return System.getProperty("os.name");
    }


    @Tool("Returns the operating system version")
    public String getOperatingSystemVersion() {

        return System.getProperty("os.version");
    }


    @Tool("Returns the operating system architecture")
    public String getOperatingSystemArchitecture() {

        return System.getProperty("os.arch");
    }


    @Tool("Returns the current Windows user running the Java application")
    public String getCurrentUser() {

        return System.getProperty("user.name");
    }


    @Tool("Returns the current working directory of the Java application")
    public String getCurrentWorkingDirectory() {

        return System.getProperty("user.dir");
    }


    // ============================================================
    // CPU
    // ============================================================

    @Tool("Retrieves the current CPU usage percentage of the host server machine")
    public double getCpuUsage() {

        double usage = osBean.getCpuLoad();

        if (usage < 0) {
            return 0;
        }

        return usage * 100;
    }


    @Tool("Retrieves the CPU usage percentage of the Java process")
    public double getProcessCpuUsage() {

        double usage = osBean.getProcessCpuLoad();

        if (usage < 0) {
            return 0;
        }

        return usage * 100;
    }


    @Tool("Returns the number of logical CPU processors available to the JVM")
    public int getAvailableProcessors() {

        return Runtime.getRuntime().availableProcessors();
    }


    // ============================================================
    // MEMORY
    // ============================================================

    @Tool("Returns the total physical memory of the server in Gigabytes")
    public double getTotalPhysicalMemoryGB() {

        long totalBytes = osBean.getTotalMemorySize();

        return bytesToGB(totalBytes);
    }


    @Tool("Returns the free physical memory of the server in Gigabytes")
    public double getFreePhysicalMemoryGB() {

        long freeBytes = osBean.getFreeMemorySize();

        return bytesToGB(freeBytes);
    }


    @Tool("Returns the percentage of physical memory currently being used")
    public double getMemoryUsagePercentage() {

        long totalBytes = osBean.getTotalMemorySize();
        long freeBytes = osBean.getFreeMemorySize();

        if (totalBytes <= 0) {
            return 0;
        }

        long usedBytes = totalBytes - freeBytes;

        return ((double) usedBytes / totalBytes) * 100;
    }


    // ============================================================
    // JVM MEMORY
    // ============================================================

    @Tool("Returns Java JVM heap memory information")
    public Map<String, Object> getJvmMemoryUsage() {

        MemoryMXBean memoryBean =
                ManagementFactory.getMemoryMXBean();

        MemoryUsage heap =
                memoryBean.getHeapMemoryUsage();

        Map<String, Object> result =
                new HashMap<>();

        result.put("initMB",
                bytesToMB(heap.getInit()));

        result.put("usedMB",
                bytesToMB(heap.getUsed()));

        result.put("committedMB",
                bytesToMB(heap.getCommitted()));

        result.put("maxMB",
                bytesToMB(heap.getMax()));

        return result;
    }


    // ============================================================
    // DISK
    // ============================================================

    @Tool("Returns the available free disk space in Gigabytes for a given folder or drive path")
    public long getFreeDiskSpace(String path) {

        File file = new File(path);

        if (!file.exists()) {
            return -1;
        }

        return bytesToGBLong(
                file.getFreeSpace()
        );
    }


    @Tool("Returns the total disk space in Gigabytes for a given folder or drive path")
    public long getTotalDiskSpace(String path) {

        File file = new File(path);

        if (!file.exists()) {
            return -1;
        }

        return bytesToGBLong(
                file.getTotalSpace()
        );
    }


    @Tool("Returns the disk usage percentage for a given folder or drive path")
    public double getDiskUsagePercentage(String path) {

        File file = new File(path);

        if (!file.exists()) {
            return -1;
        }

        long total =
                file.getTotalSpace();

        long free =
                file.getFreeSpace();

        if (total <= 0) {
            return 0;
        }

        long used =
                total - free;

        return ((double) used / total) * 100;
    }


    @Tool("Returns detailed disk information including total, used, free space and usage percentage")
    public Map<String, Object> getDiskInformation(String path) {

        File file = new File(path);

        Map<String, Object> result =
                new HashMap<>();

        if (!file.exists()) {

            result.put(
                    "error",
                    "Path does not exist"
            );

            return result;
        }

        long total =
                file.getTotalSpace();

        long free =
                file.getFreeSpace();

        long used =
                total - free;

        result.put(
                "path",
                path
        );

        result.put(
                "totalGB",
                bytesToGB(total)
        );

        result.put(
                "freeGB",
                bytesToGB(free)
        );

        result.put(
                "usedGB",
                bytesToGB(used)
        );

        result.put(
                "usagePercentage",
                total > 0
                        ? ((double) used / total) * 100
                        : 0
        );

        return result;
    }


    // ============================================================
    // JAVA
    // ============================================================

    @Tool("Returns the Java version running the application")
    public String getJavaVersion() {

        return System.getProperty(
                "java.version"
        );
    }


    @Tool("Returns the Java vendor")
    public String getJavaVendor() {

        return System.getProperty(
                "java.vendor"
        );
    }


    @Tool("Returns the Java home directory")
    public String getJavaHome() {

        return System.getProperty(
                "java.home"
        );
    }


    @Tool("Returns the JVM name")
    public String getJvmName() {

        return System.getProperty(
                "java.vm.name"
        );
    }


    @Tool("Returns the JVM version")
    public String getJvmVersion() {

        return System.getProperty(
                "java.vm.version"
        );
    }


    @Tool("Returns the JVM uptime in seconds")
    public long getJvmUptimeSeconds() {

        return ManagementFactory
                .getRuntimeMXBean()
                .getUptime() / 1000;
    }


    @Tool("Returns the JVM uptime in a human readable format")
    public String getJvmUptime() {

        long uptimeMilliseconds =
                ManagementFactory
                        .getRuntimeMXBean()
                        .getUptime();

        Duration duration =
                Duration.ofMillis(
                        uptimeMilliseconds
                );

        return String.format(
                "%d days, %d hours, %d minutes, %d seconds",
                duration.toDays(),
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart()
        );
    }


    // ============================================================
    // JVM THREADS
    // ============================================================

    @Tool("Returns the current number of live JVM threads")
    public int getLiveThreadCount() {

        return ManagementFactory
                .getThreadMXBean()
                .getThreadCount();
    }


    @Tool("Returns the peak number of JVM threads")
    public int getPeakThreadCount() {

        return ManagementFactory
                .getThreadMXBean()
                .getPeakThreadCount();
    }


    @Tool("Returns the number of daemon JVM threads")
    public int getDaemonThreadCount() {

        return ManagementFactory
                .getThreadMXBean()
                .getDaemonThreadCount();
    }


    // ============================================================
    // FILE SYSTEM
    // ============================================================

    @Tool("Checks whether a file or directory exists")
    public boolean fileExists(String path) {

        return Files.exists(
                Paths.get(path)
        );
    }


    @Tool("Checks whether a path is a directory")
    public boolean isDirectory(String path) {

        return Files.isDirectory(
                Paths.get(path)
        );
    }


    @Tool("Checks whether a path is a regular file")
    public boolean isFile(String path) {

        return Files.isRegularFile(
                Paths.get(path)
        );
    }


    @Tool("Returns the size of a file in Megabytes")
    public double getFileSizeMB(String path) {

        try {

            Path filePath =
                    Paths.get(path);

            if (!Files.exists(filePath)) {
                return -1;
            }

            return bytesToMB(
                    Files.size(filePath)
            );

        } catch (Exception e) {

            return -1;
        }
    }


    @Tool("Lists files and directories inside a specified directory")
    public List<String> listDirectory(String path) {

        List<String> result =
                new ArrayList<>();

        try {

            Path directory =
                    Paths.get(path);

            if (!Files.isDirectory(directory)) {

                result.add(
                        "Path is not a directory"
                );

                return result;
            }

            try (var stream =
                         Files.list(directory)) {

                stream.forEach(
                        p -> result.add(
                                p.getFileName()
                                        .toString()
                        )
                );
            }

        } catch (Exception e) {

            result.add(
                    "Error: " + e.getMessage()
            );
        }

        return result;
    }


    // ============================================================
    // NETWORK
    // ============================================================

    @Tool("Returns the MAC address of the primary network interface")
    public String getMacAddress() {

        try {

            InetAddress localHost =
                    InetAddress.getLocalHost();

            NetworkInterface networkInterface =
                    NetworkInterface.getByInetAddress(
                            localHost
                    );

            if (networkInterface == null) {
                return "Unknown";
            }

            byte[] mac =
                    networkInterface.getHardwareAddress();

            if (mac == null) {
                return "Unknown";
            }

            StringBuilder result =
                    new StringBuilder();

            for (byte b : mac) {

                result.append(
                        String.format(
                                "%02X:",
                                b
                        )
                );
            }

            return result
                    .substring(
                            0,
                            result.length() - 1
                    );

        } catch (Exception e) {

            return "Unknown";
        }
    }


    @Tool("Checks whether the server has network connectivity")
    public boolean isNetworkAvailable() {

        try {

            InetAddress address =
                    InetAddress.getByName(
                            "google.com"
                    );

            return address.isReachable(
                    3000
            );

        } catch (Exception e) {

            return false;
        }
    }


    // ============================================================
    // ENVIRONMENT
    // ============================================================

    @Tool("Returns the value of an environment variable")
    public String getEnvironmentVariable(
            String variableName) {

        String value =
                System.getenv(
                        variableName
                );

        if (value == null) {
            return "Environment variable not found";
        }

        return value;
    }


    @Tool("Returns the value of a Java system property")
    public String getSystemProperty(
            String propertyName) {

        return System.getProperty(
                propertyName
        );
    }


    // ============================================================
    // SYSTEM SUMMARY
    // ============================================================

    @Tool("Returns a summary of the current server including CPU, memory, operating system and Java information")
    public Map<String, Object> getSystemSummary() {

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "hostname",
                getHostname()
        );

        result.put(
                "ipAddress",
                getIpAddress()
        );

        result.put(
                "operatingSystem",
                getOperatingSystem()
        );

        result.put(
                "osVersion",
                getOperatingSystemVersion()
        );

        result.put(
                "architecture",
                getOperatingSystemArchitecture()
        );

        result.put(
                "cpuUsagePercentage",
                getCpuUsage()
        );

        result.put(
                "processCpuUsagePercentage",
                getProcessCpuUsage()
        );

        result.put(
                "logicalProcessors",
                getAvailableProcessors()
        );

        result.put(
                "totalMemoryGB",
                getTotalPhysicalMemoryGB()
        );

        result.put(
                "freeMemoryGB",
                getFreePhysicalMemoryGB()
        );

        result.put(
                "memoryUsagePercentage",
                getMemoryUsagePercentage()
        );

        result.put(
                "javaVersion",
                getJavaVersion()
        );

        result.put(
                "javaVendor",
                getJavaVendor()
        );

        result.put(
                "jvmName",
                getJvmName()
        );

        result.put(
                "jvmUptime",
                getJvmUptime()
        );

        result.put(
                "liveThreads",
                getLiveThreadCount()
        );

        return result;
    }


    // ============================================================
    // HELPER METHODS
    // ============================================================

    private double bytesToGB(long bytes) {

        return bytes /
                (1024.0 * 1024.0 * 1024.0);
    }


    private long bytesToGBLong(long bytes) {

        return bytes /
                (1024L * 1024L * 1024L);
    }


    private double bytesToMB(long bytes) {

        if (bytes < 0) {
            return -1;
        }

        return bytes /
                (1024.0 * 1024.0);
    }
}