package com.proj.ai;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//import java.lang.management.OperatingSystemMXBean;
import com.sun.management.OperatingSystemMXBean;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class SystemTools1 {
	
	@Tool("Retrieves the current CPU usage percentage of the host server machine")
	public double getCpuUsage() {
		
		OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
		double usage = osBean.getCpuLoad() * 100;
		return usage;
	}
	
	@Tool("Retrieves the available free disk space space in Gigabytes (GB) for a given folder path")
	public long getFreeDiskSpace(String path) {
		File file = new File(path);
		long freeSpaceBytes = file.getFreeSpace();
		return freeSpaceBytes / (1024 * 1024 * 1024);
	}
	
	 @Tool("Returns a test value")
	    public String ping() {
	        return "pong";
	    }
	 
	 @Tool("Returns the number of available CPU processors on the server")
	    public int getAvailableProcessors() {
	        return Runtime.getRuntime().availableProcessors();
	    }
	 
//	 @Tool("Returns a complete summary of the server system including CPU, memory, operating system, Java, and disk information")
//	    public Map<String, Object> getSystemSummary() {
//
//	        Map<String, Object> result =
//	                new HashMap<>();
//
//	        result.put("hostname", getHostname());
//	        result.put("ipAddress", getIpAddress());
//
//	        result.put("cpuUsagePercentage",
//	                getCpuUsage());
//
//	        result.put("processCpuUsagePercentage",
//	                getProcessCpuUsage());
//
//	        result.put("availableProcessors",
//	                getAvailableProcessors());
//
//	        result.put("totalMemoryGB",
//	                getTotalPhysicalMemoryGB());
//
//	        result.put("freeMemoryGB",
//	                getFreePhysicalMemoryGB());
//
//	        result.put("memoryUsagePercentage",
//	                getMemoryUsagePercentage());
//
//	        result.put("operatingSystem",
//	                getOperatingSystem());
//
//	        result.put("osVersion",
//	                getOperatingSystemVersion());
//
//	        result.put("architecture",
//	                getOperatingSystemArchitecture());
//
//	        result.put("javaVersion",
//	                getJavaVersion());
//
//	        result.put("jvmName",
//	                getJvmName());
//
//	        result.put("jvmUptime",
//	                getJvmUptime());
//
//	        result.put("liveThreads",
//	                getLiveThreadCount());
//
//	        return result;
//	    }

	 
	 // ---------------------------------------------------------
	    // ENVIRONMENT
	    // ---------------------------------------------------------

	    @Tool("Returns the value of an environment variable")
	    public String getEnvironmentVariable(String variableName) {

	        String value =
	                System.getenv(variableName);

	        if (value == null) {
	            return "Environment variable not found";
	        }

	        return value;
	    }
	    
	    @Tool("Returns whether the server has an active network connection")
	    public boolean isNetworkAvailable() {

	        try {

	            InetAddress address =
	                    InetAddress.getByName("google.com");

	            return address.isReachable(3000);

	        } catch (Exception e) {

	            return false;
	        }
	    }
	    
	    // ---------------------------------------------------------
	    // NETWORK
	    // ---------------------------------------------------------
	    @Tool("Returns the MAC address of the primary network interface")
	    public String getMacAddress() {

	        try {

	            InetAddress localHost =
	                    InetAddress.getLocalHost();

	            NetworkInterface networkInterface =
	                    NetworkInterface.getByInetAddress(localHost);

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
	                        String.format("%02X:", b)
	                );
	            }

	            return result
	                    .substring(0, result.length() - 1);

	        } catch (Exception e) {

	            return "Unable to determine MAC address";
	        }
	    }
	    
	    
	    @Tool("Lists files and directories inside the specified directory")
	    public List<String> listDirectory(String path) {

	        List<String> result = new ArrayList<>();

	        try {

	            Path directory = Paths.get(path);

	            if (!Files.isDirectory(directory)) {
	                return result;
	            }

	            Files.list(directory)
	                    .forEach(p -> result.add(
	                            p.getFileName().toString()
	                    ));

	        } catch (Exception e) {

	            result.add("Error: " + e.getMessage());
	        }

	        return result;
	    }
	    
	    @Tool("Returns the size of a file in Megabytes")
	    public double getFileSizeMB(String path) {

	        try {

	            Path filePath = Paths.get(path);

	            if (!Files.exists(filePath)) {
	                return -1;
	            }

	            return bytesToMB(Files.size(filePath));

	        } catch (Exception e) {

	            return -1;
	        }
	    }
	    
	    private double bytesToMB(long bytes) {

	        return bytes / (1024.0 * 1024.0);
	    }
	    
	    @Tool("Returns the peak number of JVM threads")
	    public int getPeakThreadCount() {

	        return ManagementFactory
	                .getThreadMXBean()
	                .getPeakThreadCount();
	    }
	    
	    @Tool("Returns the current number of JVM live threads")
	    public int getLiveThreadCount() {

	        return ManagementFactory
	                .getThreadMXBean()
	                .getThreadCount();
	    }
	    
	    
	    @Tool("Returns the JVM uptime in a human readable format")
	    public String getJvmUptime() {

	        long milliseconds =
	                ManagementFactory
	                        .getRuntimeMXBean()
	                        .getUptime();

	        Duration duration =
	                Duration.ofMillis(milliseconds);

	        return String.format(
	                "%d days, %d hours, %d minutes, %d seconds",
	                duration.toDays(),
	                duration.toHoursPart(),
	                duration.toMinutesPart(),
	                duration.toSecondsPart()
	        );
	    }
	    
	    // ---------------------------------------------------------
	    // JAVA / JVM
	    // ---------------------------------------------------------

	    @Tool("Returns the Java version running the application")
	    public String getJavaVersion() {
	        return System.getProperty("java.version");
	    }

	    @Tool("Returns the Java vendor")
	    public String getJavaVendor() {
	        return System.getProperty("java.vendor");
	    }

	    @Tool("Returns the JVM name")
	    public String getJvmName() {
	        return System.getProperty("java.vm.name");
	    }
	    
	    // ---------------------------------------------------------
	    // OPERATING SYSTEM
	    // ---------------------------------------------------------

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

	    @Tool("Returns the current user running the Java application")
	    public String getCurrentUser() {
	        return System.getProperty("user.name");
	    }

	    @Tool("Returns the current working directory of the Java application")
	    public String getCurrentWorkingDirectory() {
	        return System.getProperty("user.dir");
	    }
	    
	  
}
