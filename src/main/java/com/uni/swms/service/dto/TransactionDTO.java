package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private Integer quantity;

    @NotNull
    private Instant date;

    private String notes;

    @NotNull
    private ProductDTO product;

    private WarehouseLocationDTO sourceLocation;

    private WarehouseLocationDTO targetLocation;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public WarehouseLocationDTO getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(WarehouseLocationDTO sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public WarehouseLocationDTO getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(WarehouseLocationDTO targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", date='" + getDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", product=" + getProduct() +
            ", sourceLocation=" + getSourceLocation() +
            ", targetLocation=" + getTargetLocation() +
            "}";
    }
}
