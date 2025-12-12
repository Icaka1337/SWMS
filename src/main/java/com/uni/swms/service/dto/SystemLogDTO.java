package com.uni.swms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.uni.swms.domain.SystemLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemLogDTO implements Serializable {

    private Long id;

    private String username;

    private String action;

    private String entityName;

    private String details;

    @NotNull
    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemLogDTO)) {
            return false;
        }

        SystemLogDTO systemLogDTO = (SystemLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemLogDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", action='" + getAction() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", details='" + getDetails() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
