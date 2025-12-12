package com.uni.swms.domain;

import static com.uni.swms.domain.InventoryItemTestSamples.*;
import static com.uni.swms.domain.ProductTestSamples.*;
import static com.uni.swms.domain.WarehouseLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItem.class);
        InventoryItem inventoryItem1 = getInventoryItemSample1();
        InventoryItem inventoryItem2 = new InventoryItem();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);

        inventoryItem2.setId(inventoryItem1.getId());
        assertThat(inventoryItem1).isEqualTo(inventoryItem2);

        inventoryItem2 = getInventoryItemSample2();
        assertThat(inventoryItem1).isNotEqualTo(inventoryItem2);
    }

    @Test
    void productTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        inventoryItem.setProduct(productBack);
        assertThat(inventoryItem.getProduct()).isEqualTo(productBack);

        inventoryItem.product(null);
        assertThat(inventoryItem.getProduct()).isNull();
    }

    @Test
    void locationTest() {
        InventoryItem inventoryItem = getInventoryItemRandomSampleGenerator();
        WarehouseLocation warehouseLocationBack = getWarehouseLocationRandomSampleGenerator();

        inventoryItem.setLocation(warehouseLocationBack);
        assertThat(inventoryItem.getLocation()).isEqualTo(warehouseLocationBack);

        inventoryItem.location(null);
        assertThat(inventoryItem.getLocation()).isNull();
    }
}
