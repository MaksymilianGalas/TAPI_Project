package com.microservices.documentservice.generator;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Slf4j
public class ExcelGenerator {

    public byte[] generateOrderReportExcel(Map<String, Object> data) {
        log.info("Generating order report Excel");

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Order Report");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("ORDER REPORT");
            titleCell.setCellStyle(headerStyle);

            // Date row
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue("Generated:");
            dateRow.createCell(1)
                    .setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Empty row
            sheet.createRow(2);

            // Headers
            Row headerRow = sheet.createRow(3);
            String[] headers = { "Order Number", "Customer", "Items", "Total Amount", "Status", "Date" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Sample data rows (in real scenario, this would come from data)
            int rowNum = 4;
            String ordersData = (String) data.getOrDefault("orders", "ORD-001|John Doe|3|500.00|CONFIRMED|2024-01-20");
            for (String orderLine : ordersData.split(";")) {
                Row row = sheet.createRow(rowNum++);
                String[] orderParts = orderLine.split("\\|");
                for (int i = 0; i < orderParts.length; i++) {
                    row.createCell(i).setCellValue(orderParts[i]);
                }
            }

            // Summary section
            rowNum++;
            Row summaryTitleRow = sheet.createRow(rowNum++);
            Cell summaryCell = summaryTitleRow.createCell(0);
            summaryCell.setCellValue("SUMMARY");
            summaryCell.setCellStyle(headerStyle);

            Row totalOrdersRow = sheet.createRow(rowNum++);
            totalOrdersRow.createCell(0).setCellValue("Total Orders:");
            totalOrdersRow.createCell(1).setCellValue(data.getOrDefault("totalOrders", "0").toString());

            Row totalRevenueRow = sheet.createRow(rowNum++);
            totalRevenueRow.createCell(0).setCellValue("Total Revenue:");
            totalRevenueRow.createCell(1).setCellValue("$" + data.getOrDefault("totalRevenue", "0.00"));

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }

    public byte[] generateUserReportExcel(Map<String, Object> data) {
        log.info("Generating user report Excel");

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("User Report");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("USER REPORT");
            titleCell.setCellStyle(headerStyle);

            // Date row
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue("Generated:");
            dateRow.createCell(1)
                    .setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Empty row
            sheet.createRow(2);

            // Headers
            Row headerRow = sheet.createRow(3);
            String[] headers = { "Username", "Email", "First Name", "Last Name", "Status", "Created Date" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Sample data rows
            int rowNum = 4;
            String usersData = (String) data.getOrDefault("users",
                    "john.doe|john@example.com|John|Doe|Active|2024-01-15");
            for (String userLine : usersData.split(";")) {
                Row row = sheet.createRow(rowNum++);
                String[] userParts = userLine.split("\\|");
                for (int i = 0; i < userParts.length; i++) {
                    row.createCell(i).setCellValue(userParts[i]);
                }
            }

            // Summary
            rowNum++;
            Row summaryRow = sheet.createRow(rowNum++);
            Cell summaryCell = summaryRow.createCell(0);
            summaryCell.setCellValue("SUMMARY");
            summaryCell.setCellStyle(headerStyle);

            Row totalUsersRow = sheet.createRow(rowNum++);
            totalUsersRow.createCell(0).setCellValue("Total Users:");
            totalUsersRow.createCell(1).setCellValue(data.getOrDefault("totalUsers", "0").toString());

            Row activeUsersRow = sheet.createRow(rowNum++);
            activeUsersRow.createCell(0).setCellValue("Active Users:");
            activeUsersRow.createCell(1).setCellValue(data.getOrDefault("activeUsers", "0").toString());

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating user Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate user Excel", e);
        }
    }
}
