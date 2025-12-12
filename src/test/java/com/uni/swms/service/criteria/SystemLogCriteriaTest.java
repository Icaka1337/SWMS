package com.uni.swms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SystemLogCriteriaTest {

    @Test
    void newSystemLogCriteriaHasAllFiltersNullTest() {
        var systemLogCriteria = new SystemLogCriteria();
        assertThat(systemLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void systemLogCriteriaFluentMethodsCreatesFiltersTest() {
        var systemLogCriteria = new SystemLogCriteria();

        setAllFilters(systemLogCriteria);

        assertThat(systemLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void systemLogCriteriaCopyCreatesNullFilterTest() {
        var systemLogCriteria = new SystemLogCriteria();
        var copy = systemLogCriteria.copy();

        assertThat(systemLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(systemLogCriteria)
        );
    }

    @Test
    void systemLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var systemLogCriteria = new SystemLogCriteria();
        setAllFilters(systemLogCriteria);

        var copy = systemLogCriteria.copy();

        assertThat(systemLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(systemLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var systemLogCriteria = new SystemLogCriteria();

        assertThat(systemLogCriteria).hasToString("SystemLogCriteria{}");
    }

    private static void setAllFilters(SystemLogCriteria systemLogCriteria) {
        systemLogCriteria.id();
        systemLogCriteria.username();
        systemLogCriteria.action();
        systemLogCriteria.entityName();
        systemLogCriteria.details();
        systemLogCriteria.timestamp();
        systemLogCriteria.distinct();
    }

    private static Condition<SystemLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getEntityName()) &&
                condition.apply(criteria.getDetails()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SystemLogCriteria> copyFiltersAre(SystemLogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getEntityName(), copy.getEntityName()) &&
                condition.apply(criteria.getDetails(), copy.getDetails()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
