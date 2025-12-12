package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.Supplier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String contactInfo;

    private Double reliabilityScore;

    private String email;

    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Double getReliabilityScore() {
        return reliabilityScore;
    }

    public void setReliabilityScore(Double reliabilityScore) {
        this.reliabilityScore = reliabilityScore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierDTO)) {
            return false;
        }

        SupplierDTO supplierDTO = (SupplierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supplierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            ", reliabilityScore=" + getReliabilityScore() +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
