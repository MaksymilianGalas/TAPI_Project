package com.microservices.documentservice.service;

import com.microservices.documentservice.generator.ExcelGenerator;
import com.microservices.documentservice.generator.PdfGenerator;
import com.microservices.documentservice.model.DocumentMetadata;
import com.microservices.documentservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PdfGenerator pdfGenerator;
    private final ExcelGenerator excelGenerator;

    public List<DocumentMetadata> getAllDocuments() {
        log.info("Fetching all documents");
        return documentRepository.findAll();
    }

    public Optional<DocumentMetadata> getDocumentById(String id) {
        log.info("Fetching document by id: {}", id);
        return documentRepository.findById(id);
    }

    public byte[] generateDocument(String templateType, String documentType, Map<String, Object> data,
            String generatedBy) {
        log.info("Generating document - Template: {}, Type: {}", templateType, documentType);

        byte[] documentBytes;

        if ("PDF".equalsIgnoreCase(documentType)) {
            documentBytes = generatePdfDocument(templateType, data);
        } else if ("EXCEL".equalsIgnoreCase(documentType)) {
            documentBytes = generateExcelDocument(templateType, data);
        } else {
            throw new RuntimeException("Unsupported document type: " + documentType);
        }

        // Save metadata
        DocumentMetadata metadata = new DocumentMetadata();
        metadata.setDocumentName(templateType + "_" + System.currentTimeMillis());
        metadata.setDocumentType(documentType);
        metadata.setTemplateType(templateType);
        metadata.setGeneratedBy(generatedBy);
        metadata.setMetadata(data);
        metadata.setCreatedAt(LocalDateTime.now());

        documentRepository.save(metadata);

        return documentBytes;
    }

    private byte[] generatePdfDocument(String templateType, Map<String, Object> data) {
        return switch (templateType.toUpperCase()) {
            case "INVOICE" -> pdfGenerator.generateInvoicePdf(data);
            case "REPORT" -> pdfGenerator.generateReportPdf(data);
            default -> throw new RuntimeException("Unsupported PDF template type: " + templateType);
        };
    }

    private byte[] generateExcelDocument(String templateType, Map<String, Object> data) {
        return switch (templateType.toUpperCase()) {
            case "ORDER_REPORT" -> excelGenerator.generateOrderReportExcel(data);
            case "USER_REPORT" -> excelGenerator.generateUserReportExcel(data);
            default -> throw new RuntimeException("Unsupported Excel template type: " + templateType);
        };
    }

    public void deleteDocument(String id) {
        log.info("Deleting document: {}", id);

        DocumentMetadata document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        documentRepository.delete(document);
    }
}
