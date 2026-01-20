package com.microservices.documentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documents")
public class DocumentMetadata {

    @Id
    private String id;

    @NotBlank(message = "Document name is required")
    private String documentName;

    @NotBlank(message = "Document type is required")
    private String documentType; // PDF, EXCEL

    @NotBlank(message = "Template type is required")
    private String templateType; // INVOICE, REPORT, ORDER_SUMMARY

    private String generatedBy;

    private Map<String, Object> metadata;

    private LocalDateTime createdAt;
}
