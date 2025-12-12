package com.uni.swms.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemCriteriaTest {

    @Test
    void newInventoryItemCriteriaHasAllFiltersNullTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        setAllFilters(inventoryItemCriteria);

        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void inventoryItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        setAllFilters(inventoryItemCriteria);

        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        assertThat(inventoryItemCriteria).hasToString("InventoryItemCriteria{}");
    }

    private static void setAllFilters(InventoryItemCriteria inventoryItemCriteria) {
        inventoryItemCriteria.id();
        inventoryItemCriteria.quantity();
        inventoryItemCriteria.lastUpdated();
        inventoryItemCriteria.productId();
        inventoryItemCriteria.locationId();
        inventoryItemCriteria.distinct();
    }

    private static Condition<InventoryItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemCriteria> copyFiltersAre(
        InventoryItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
