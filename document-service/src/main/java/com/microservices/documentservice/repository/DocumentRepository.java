package com.microservices.documentservice.repository;

import com.microservices.documentservice.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findByDocumentType(String documentType);

    List<DocumentMetadata> findByTemplateType(String templateType);

    List<DocumentMetadata> findByGeneratedBy(String generatedBy);
}
