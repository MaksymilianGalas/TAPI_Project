package com.microservices.documentservice.generator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Slf4j
public class PdfGenerator {

    public byte[] generateInvoicePdf(Map<String, Object> data) {
        log.info("Generating invoice PDF");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Title
            Paragraph title = new Paragraph("INVOICE")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Invoice details
            String invoiceNumber = (String) data.getOrDefault("invoiceNumber", "INV-001");
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            document.add(new Paragraph("Invoice Number: " + invoiceNumber).setBold());
            document.add(new Paragraph("Date: " + date));
            document.add(new Paragraph("\n"));

            // Customer details
            document.add(new Paragraph("Customer Information").setBold().setFontSize(14));
            document.add(new Paragraph("Name: " + data.getOrDefault("customerName", "N/A")));
            document.add(new Paragraph("Email: " + data.getOrDefault("customerEmail", "N/A")));
            document.add(new Paragraph("Address: " + data.getOrDefault("customerAddress", "N/A")));
            document.add(new Paragraph("\n"));

            // Items table
            document.add(new Paragraph("Items").setBold().setFontSize(14));

            Table table = new Table(UnitValue.createPercentArray(new float[] { 3, 1, 2, 2 }));
            table.setWidth(UnitValue.createPercentValue(100));

            // Table headers
            table.addHeaderCell("Item");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Price");
            table.addHeaderCell("Subtotal");

            // Sample items (in real scenario, this would come from data)
            String items = (String) data.getOrDefault("items", "Product A|2|100.00|200.00");
            for (String item : items.split(";")) {
                String[] parts = item.split("\\|");
                for (String part : parts) {
                    table.addCell(part);
                }
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // Total
            Paragraph total = new Paragraph("Total Amount: $" + data.getOrDefault("totalAmount", "0.00"))
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Thank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public byte[] generateReportPdf(Map<String, Object> data) {
        log.info("Generating report PDF");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Title
            Paragraph title = new Paragraph("BUSINESS REPORT")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Report details
            String reportTitle = (String) data.getOrDefault("reportTitle", "Monthly Report");
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            document.add(new Paragraph("Report: " + reportTitle).setBold().setFontSize(16));
            document.add(new Paragraph("Generated: " + date));
            document.add(new Paragraph("\n"));

            // Summary
            document.add(new Paragraph("Summary").setBold().setFontSize(14));
            document.add(new Paragraph((String) data.getOrDefault("summary", "This is a sample report summary.")));
            document.add(new Paragraph("\n"));

            // Statistics
            document.add(new Paragraph("Statistics").setBold().setFontSize(14));
            document.add(new Paragraph("Total Orders: " + data.getOrDefault("totalOrders", "0")));
            document.add(new Paragraph("Total Revenue: $" + data.getOrDefault("totalRevenue", "0.00")));
            document.add(new Paragraph("Active Users: " + data.getOrDefault("activeUsers", "0")));

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating report PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate report PDF", e);
        }
    }
}
