package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.AIInsight} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIInsightDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private String message;

    private Double confidence;

    @NotNull
    private Instant generatedAt;

    @NotNull
    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AIInsightDTO)) {
            return false;
        }

        AIInsightDTO aIInsightDTO = (AIInsightDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aIInsightDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIInsightDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", message='" + getMessage() + "'" +
            ", confidence=" + getConfidence() +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
