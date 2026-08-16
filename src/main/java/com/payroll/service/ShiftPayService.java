//package com.payroll.service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//// Interfaces representing your API clients / Feign Clients / REST Clients
//interface EmployeeClient  {
//    BigDecimal getHourlyRate(Long employeeId);
//}
//
//interface HolidayClient {
//    boolean isPublicHoliday(LocalDate date);
//}
//
//public class ShiftPayService {
//
//    private final ShiftPayService employeeClient;
//    private final HolidayClient holidayClient;
//
//    // Dependencies are injected via constructor
//    public ShiftPayService(ShiftPayService employeeClient, HolidayClient holidayClient) {
//        this.employeeClient = employeeClient;
//        this.holidayClient = holidayClient;
//    }
//
//    public BigDecimal calculateShiftPay(Long employeeId, LocalDate date, double hoursWorked) {
//        // External API Call 1
//        BigDecimal baseRate = employeeClient.getHourlyRate(employeeId);
//        
//        BigDecimal finalRate = baseRate;
//
//        // External API Call 2
//        if (holidayClient.isPublicHoliday(date)) {
//            finalRate = finalRate.multiply(new BigDecimal("1.50")); // 50% Holiday Premium
//        }
//
//        return finalRate.multiply(BigDecimal.valueOf(hoursWorked));
//    }
//}
