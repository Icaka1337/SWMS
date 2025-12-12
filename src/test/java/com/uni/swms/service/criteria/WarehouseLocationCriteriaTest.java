package com.uni.swms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WarehouseLocationCriteriaTest {

    @Test
    void newWarehouseLocationCriteriaHasAllFiltersNullTest() {
        var warehouseLocationCriteria = new WarehouseLocationCriteria();
        assertThat(warehouseLocationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void warehouseLocationCriteriaFluentMethodsCreatesFiltersTest() {
        var warehouseLocationCriteria = new WarehouseLocationCriteria();

        setAllFilters(warehouseLocationCriteria);

        assertThat(warehouseLocationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void warehouseLocationCriteriaCopyCreatesNullFilterTest() {
        var warehouseLocationCriteria = new WarehouseLocationCriteria();
        var copy = warehouseLocationCriteria.copy();

        assertThat(warehouseLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(warehouseLocationCriteria)
        );
    }

    @Test
    void warehouseLocationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var warehouseLocationCriteria = new WarehouseLocationCriteria();
        setAllFilters(warehouseLocationCriteria);

        var copy = warehouseLocationCriteria.copy();

        assertThat(warehouseLocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(warehouseLocationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var warehouseLocationCriteria = new WarehouseLocationCriteria();

        assertThat(warehouseLocationCriteria).hasToString("WarehouseLocationCriteria{}");
    }

    private static void setAllFilters(WarehouseLocationCriteria warehouseLocationCriteria) {
        warehouseLocationCriteria.id();
        warehouseLocationCriteria.name();
        warehouseLocationCriteria.section();
        warehouseLocationCriteria.capacity();
        warehouseLocationCriteria.description();
        warehouseLocationCriteria.distinct();
    }

    private static Condition<WarehouseLocationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSection()) &&
                condition.apply(criteria.getCapacity()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WarehouseLocationCriteria> copyFiltersAre(
        WarehouseLocationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSection(), copy.getSection()) &&
                condition.apply(criteria.getCapacity(), copy.getCapacity()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
