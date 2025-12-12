package com.uni.swms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AIInsightCriteriaTest {

    @Test
    void newAIInsightCriteriaHasAllFiltersNullTest() {
        var aIInsightCriteria = new AIInsightCriteria();
        assertThat(aIInsightCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aIInsightCriteriaFluentMethodsCreatesFiltersTest() {
        var aIInsightCriteria = new AIInsightCriteria();

        setAllFilters(aIInsightCriteria);

        assertThat(aIInsightCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aIInsightCriteriaCopyCreatesNullFilterTest() {
        var aIInsightCriteria = new AIInsightCriteria();
        var copy = aIInsightCriteria.copy();

        assertThat(aIInsightCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aIInsightCriteria)
        );
    }

    @Test
    void aIInsightCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aIInsightCriteria = new AIInsightCriteria();
        setAllFilters(aIInsightCriteria);

        var copy = aIInsightCriteria.copy();

        assertThat(aIInsightCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aIInsightCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aIInsightCriteria = new AIInsightCriteria();

        assertThat(aIInsightCriteria).hasToString("AIInsightCriteria{}");
    }

    private static void setAllFilters(AIInsightCriteria aIInsightCriteria) {
        aIInsightCriteria.id();
        aIInsightCriteria.type();
        aIInsightCriteria.message();
        aIInsightCriteria.confidence();
        aIInsightCriteria.generatedAt();
        aIInsightCriteria.productId();
        aIInsightCriteria.distinct();
    }

    private static Condition<AIInsightCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getGeneratedAt()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AIInsightCriteria> copyFiltersAre(AIInsightCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getGeneratedAt(), copy.getGeneratedAt()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
