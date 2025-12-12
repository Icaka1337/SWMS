package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.WarehouseLocation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseLocationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String section;

    private Integer capacity;

    private String description;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarehouseLocationDTO)) {
            return false;
        }

        WarehouseLocationDTO warehouseLocationDTO = (WarehouseLocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warehouseLocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarehouseLocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", section='" + getSection() + "'" +
            ", capacity=" + getCapacity() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
