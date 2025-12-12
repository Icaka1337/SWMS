package com.uni.swms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InventoryItem.
 */
@Entity
@Table(name = "inventory_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "supplier" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    private WarehouseLocation location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public InventoryItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public InventoryItem lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InventoryItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public WarehouseLocation getLocation() {
        return this.location;
    }

    public void setLocation(WarehouseLocation warehouseLocation) {
        this.location = warehouseLocation;
    }

    public InventoryItem location(WarehouseLocation warehouseLocation) {
        this.setLocation(warehouseLocation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryItem)) {
            return false;
        }
        return getId() != null && getId().equals(((InventoryItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
