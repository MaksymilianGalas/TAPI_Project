package com.microservices.documentservice.controller;

import com.microservices.documentservice.model.DocumentMetadata;
import com.microservices.documentservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<List<DocumentMetadata>> getAllDocuments() {
        log.info("GET /api/documents - Fetching all documents");
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<DocumentMetadata> getDocumentById(@PathVariable String id) {
        log.info("GET /api/documents/{} - Fetching document by id", id);
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/generate/pdf/invoice")
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<byte[]> generateInvoicePdf(@RequestBody Map<String, Object> data,
            Authentication authentication) {
        log.info("POST /api/documents/generate/pdf/invoice - Generating invoice PDF");

        try {
            String username = authentication.getName();
            byte[] pdfBytes = documentService.generateDocument("INVOICE", "PDF", data, username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + System.currentTimeMillis() + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating invoice PDF: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate/pdf/report")
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<byte[]> generateReportPdf(@RequestBody Map<String, Object> data,
            Authentication authentication) {
        log.info("POST /api/documents/generate/pdf/report - Generating report PDF");

        try {
            String username = authentication.getName();
            byte[] pdfBytes = documentService.generateDocument("REPORT", "PDF", data, username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report_" + System.currentTimeMillis() + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating report PDF: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate/excel/orders")
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<byte[]> generateOrderReportExcel(@RequestBody Map<String, Object> data,
            Authentication authentication) {
        log.info("POST /api/documents/generate/excel/orders - Generating order report Excel");

        try {
            String username = authentication.getName();
            byte[] excelBytes = documentService.generateDocument("ORDER_REPORT", "EXCEL", data, username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "order_report_" + System.currentTimeMillis() + ".xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating order Excel: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate/excel/users")
    @PreAuthorize("hasAnyAuthority('SCOPE_user', 'SCOPE_admin')")
    public ResponseEntity<byte[]> generateUserReportExcel(@RequestBody Map<String, Object> data,
            Authentication authentication) {
        log.info("POST /api/documents/generate/excel/users - Generating user report Excel");

        try {
            String username = authentication.getName();
            byte[] excelBytes = documentService.generateDocument("USER_REPORT", "EXCEL", data, username);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "user_report_" + System.currentTimeMillis() + ".xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating user Excel: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        log.info("DELETE /api/documents/{} - Deleting document", id);
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting document: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
