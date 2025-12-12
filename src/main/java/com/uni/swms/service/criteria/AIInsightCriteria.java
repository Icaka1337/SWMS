package com.uni.swms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.uni.swms.domain.AIInsight} entity. This class is used
 * in {@link com.uni.swms.web.rest.AIInsightResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ai-insights?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIInsightCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter message;

    private DoubleFilter confidence;

    private InstantFilter generatedAt;

    private LongFilter productId;

    private Boolean distinct;

    public AIInsightCriteria() {}

    public AIInsightCriteria(AIInsightCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.generatedAt = other.optionalGeneratedAt().map(InstantFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AIInsightCriteria copy() {
        return new AIInsightCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public DoubleFilter getConfidence() {
        return confidence;
    }

    public Optional<DoubleFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public DoubleFilter confidence() {
        if (confidence == null) {
            setConfidence(new DoubleFilter());
        }
        return confidence;
    }

    public void setConfidence(DoubleFilter confidence) {
        this.confidence = confidence;
    }

    public InstantFilter getGeneratedAt() {
        return generatedAt;
    }

    public Optional<InstantFilter> optionalGeneratedAt() {
        return Optional.ofNullable(generatedAt);
    }

    public InstantFilter generatedAt() {
        if (generatedAt == null) {
            setGeneratedAt(new InstantFilter());
        }
        return generatedAt;
    }

    public void setGeneratedAt(InstantFilter generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AIInsightCriteria that = (AIInsightCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(message, that.message) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(generatedAt, that.generatedAt) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, message, confidence, generatedAt, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIInsightCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalGeneratedAt().map(f -> "generatedAt=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
