package com.uni.swms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "supplier" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private WarehouseLocation sourceLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private WarehouseLocation targetLocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Transaction type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Transaction quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getDate() {
        return this.date;
    }

    public Transaction date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getNotes() {
        return this.notes;
    }

    public Transaction notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Transaction product(Product product) {
        this.setProduct(product);
        return this;
    }

    public WarehouseLocation getSourceLocation() {
        return this.sourceLocation;
    }

    public void setSourceLocation(WarehouseLocation warehouseLocation) {
        this.sourceLocation = warehouseLocation;
    }

    public Transaction sourceLocation(WarehouseLocation warehouseLocation) {
        this.setSourceLocation(warehouseLocation);
        return this;
    }

    public WarehouseLocation getTargetLocation() {
        return this.targetLocation;
    }

    public void setTargetLocation(WarehouseLocation warehouseLocation) {
        this.targetLocation = warehouseLocation;
    }

    public Transaction targetLocation(WarehouseLocation warehouseLocation) {
        this.setTargetLocation(warehouseLocation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", quantity=" + getQuantity() +
            ", date='" + getDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
