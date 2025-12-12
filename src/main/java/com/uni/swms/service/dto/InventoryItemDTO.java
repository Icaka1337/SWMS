package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.InventoryItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    private Instant lastUpdated;

    @NotNull
    private ProductDTO product;

    @NotNull
    private WarehouseLocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public WarehouseLocationDTO getLocation() {
        return location;
    }

    public void setLocation(WarehouseLocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItemDTO)) {
            return false;
        }

        InventoryItemDTO inventoryItemDTO = (InventoryItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", product=" + getProduct() +
            ", location=" + getLocation() +
            "}";
    }
}
