//package com.payroll.controller;
//
//import com.payroll.dto.ShiftPayRequest;
//import com.payroll.service.ShiftPayService;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/payroll")
//public class PayrollController {
//
//    private final ShiftPayService shiftPayService;
//
//    public PayrollController(ShiftPayService shiftPayService) {
//        this.shiftPayService = shiftPayService;
//    }
//
//    /**
//     * Endpoint 1: Calculate single shift pay via JSON Body
//     * POST http://localhost:8080/api/v1/payroll/calculate
//     */
//    @PostMapping("/calculate")
//    public ResponseEntity<Map<String, Object>> calculateShiftPay(@RequestBody ShiftPayRequest request) {
//        // Validation check
//        if (request.getEmployeeId() == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Employee ID must not be null"));
//        }
//
//        BigDecimal totalPay = shiftPayService.calculatePay(
//                request.getEmployeeId(), 
//                request.getShiftStart(), 
//                request.getHoursWorked()
//        );
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "SUCCESS");
//        response.put("employeeId", request.getEmployeeId());
//        response.put("hoursWorked", request.getHoursWorked());
//        response.put("totalCalculatedPay", totalPay);
//        response.put("currency", "USD");
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Endpoint 2: Quick estimate calculation via Query Parameters
//     * GET http://localhost:8080/api/v1/payroll/estimate
//     */
//    @GetMapping("/estimate")
//    public ResponseEntity<Map<String, Object>> estimateShiftPay(
//            @RequestParam Long employeeId,
//            @RequestParam double hours,
//            @RequestParam boolean isNightShift,
//            @RequestParam boolean isWeekend) {
//        
//        // Simulating quick logic evaluation or alternate service call
//        BigDecimal rate = new BigDecimal("25.00");
//        if (isNightShift) rate = rate.multiply(new BigDecimal("1.15"));
//        if (isWeekend) rate = rate.multiply(new BigDecimal("1.25"));
//        BigDecimal finalEstimate = rate.multiply(BigDecimal.valueOf(hours));
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("type", "ESTIMATE");
//        response.put("employeeId", employeeId);
//        response.put("estimatedPay", finalEstimate);
//        
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Endpoint 3: Fetch historical total pay for an employee over a date range
//     * GET http://localhost:8080/api/v1/payroll/history/101?startDate=2026-08-01&endDate=2026-08-10
//     */
//    @GetMapping("/history/{employeeId}")
//    public ResponseEntity<Map<String, Object>> getPayrollHistory(
//            @PathVariable Long employeeId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        
//        // Logically you would retrieve this from database repository layers
//        Map<String, Object> response = new HashMap<>();
//        response.put("employeeId", employeeId);
//        response.put("rangeStart", startDate);
//        response.put("rangeEnd", endDate);
//        response.put("totalShiftsWorked", 12);
//        response.put("accumulatedGrossPay", new BigDecimal("3450.00"));
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Controller-Level Exception Handler 
//     * Intercepts calculation errors automatically and sends an elegant error JSON to Postman
//     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, Object>> handleInvalidInputs(IllegalArgumentException ex) {
//        Map<String, Object> errorDetails = new HashMap<>();
//        errorDetails.put("status", "BAD_REQUEST");
//        errorDetails.put("message", ex.getMessage());
//        errorDetails.put("timestamp", java.time.LocalDateTime.now());
//        
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
//    }
//}
